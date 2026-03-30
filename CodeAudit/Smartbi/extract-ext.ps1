param(
    [Alias("s")]
    [string]$Source = ".",

    [Alias("o")]
    [string]$Output = "",

    [Alias("e")]
    [string]$Ext = "",

    [Alias("k")]
    [string]$KeepStructure = ""
)

$ErrorActionPreference = "Stop"

if ([string]::IsNullOrWhiteSpace($Ext)) {
    $Ext = Read-Host "Extensions (default: jsp, supports comma or space)"
    if ([string]::IsNullOrWhiteSpace($Ext)) {
        $Ext = "jsp"
    }
}

if ([string]::IsNullOrWhiteSpace($Output)) {
    $Output = Read-Host "Output directory (default: output)"
    if ([string]::IsNullOrWhiteSpace($Output)) {
        $Output = "output"
    }
}

if ([string]::IsNullOrWhiteSpace($KeepStructure)) {
    $keepStructureInput = Read-Host "Keep original directory structure? (Y/n, default: Y)"
    if ([string]::IsNullOrWhiteSpace($keepStructureInput)) {
        $KeepStructure = "true"
    } else {
        $KeepStructure = $keepStructureInput
    }
}

$keepStructureEnabled = $false

switch ($KeepStructure.Trim().ToLowerInvariant()) {
    "y" { $keepStructureEnabled = $true }
    "yes" { $keepStructureEnabled = $true }
    "true" { $keepStructureEnabled = $true }
    "1" { $keepStructureEnabled = $true }
    "n" { $keepStructureEnabled = $false }
    "no" { $keepStructureEnabled = $false }
    "false" { $keepStructureEnabled = $false }
    "0" { $keepStructureEnabled = $false }
    default {
        throw "KeepStructure must be one of: Y, Yes, N, No, True, False, 1, 0."
    }
}

$sourcePath = (Resolve-Path -LiteralPath $Source).Path

if ([System.IO.Path]::IsPathRooted($Output)) {
    $outputPath = $Output
} else {
    $outputPath = Join-Path -Path $sourcePath -ChildPath $Output
}

$normalizedSourcePath = [System.IO.Path]::GetFullPath($sourcePath)
$normalizedOutputPath = [System.IO.Path]::GetFullPath($outputPath)

$normalizedExtensions = $Ext -split "[;,\s]+" |
    Where-Object { -not [string]::IsNullOrWhiteSpace($_) } |
    ForEach-Object {
        $item = $_.Trim().ToLowerInvariant()
        if ($item.StartsWith(".")) {
            $item
        } else {
            ".{0}" -f $item
        }
    } |
    Select-Object -Unique

if ($normalizedExtensions.Count -eq 0) {
    throw "At least one file extension must be provided."
}

$invalidFileNameCharsPattern = "[{0}]" -f [Regex]::Escape(([string][System.IO.Path]::GetInvalidFileNameChars()))

$matchedFiles = Get-ChildItem -Path $normalizedSourcePath -Recurse -File |
    Where-Object {
        $fullName = [System.IO.Path]::GetFullPath($_.FullName)
        (-not $fullName.StartsWith($normalizedOutputPath, [System.StringComparison]::OrdinalIgnoreCase)) -and
        ($normalizedExtensions -contains $_.Extension.ToLowerInvariant())
    }

$duplicateNameMap = @{}

if (-not $keepStructureEnabled) {
    $matchedFiles |
        Group-Object -Property Name |
        Where-Object { $_.Count -gt 1 } |
        ForEach-Object {
            $duplicateNameMap[$_.Name] = $true
        }
}

if (-not (Test-Path -LiteralPath $normalizedOutputPath)) {
    New-Item -ItemType Directory -Path $normalizedOutputPath | Out-Null
}

foreach ($item in $matchedFiles) {
    $relativePath = $item.FullName.Substring($normalizedSourcePath.Length).TrimStart('\\')

    if ($keepStructureEnabled) {
        $targetPath = Join-Path -Path $normalizedOutputPath -ChildPath $relativePath
    } else {
        if ($duplicateNameMap.ContainsKey($item.Name)) {
            $flattenedName = $relativePath -replace "[\\/]+", "."
            $flattenedName = $flattenedName -replace $invalidFileNameCharsPattern, "_"
            $targetFileName = $flattenedName
        } else {
            $targetFileName = $item.Name
        }

        $targetPath = Join-Path -Path $normalizedOutputPath -ChildPath $targetFileName

        if (Test-Path -LiteralPath $targetPath) {
            $baseName = [System.IO.Path]::GetFileNameWithoutExtension($targetFileName)
            $extension = [System.IO.Path]::GetExtension($targetFileName)
            $index = 1

            do {
                $candidateName = "{0}.{1}{2}" -f $baseName, $index, $extension
                $targetPath = Join-Path -Path $normalizedOutputPath -ChildPath $candidateName
                $index++
            } while (Test-Path -LiteralPath $targetPath)
        }
    }

    $targetDir = Split-Path -Path $targetPath -Parent

    if (-not (Test-Path -LiteralPath $targetDir)) {
        New-Item -ItemType Directory -Path $targetDir -Force | Out-Null
    }

    Copy-Item -LiteralPath $item.FullName -Destination $targetPath -Force
}

Write-Host ("Done. Matched extensions: {0}" -f ($normalizedExtensions -join ", "))
Write-Host ("Output directory: {0}" -f $normalizedOutputPath)
Write-Host ("Keep original structure: {0}" -f $keepStructureEnabled)
Read-Host "Press Enter to exit"

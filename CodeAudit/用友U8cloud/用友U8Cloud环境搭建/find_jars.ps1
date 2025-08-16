param(
    [string]$ModuleDir
)

if (-not $ModuleDir) {
    Write-Host "Please provide the path to the U8Cloud modules directory."
    Write-Host "Example: .\find_jars.ps1 'D:\u8cloud\modules'"
    exit
}

Write-Host "Scanning directory: $ModuleDir"
Write-Host "------------------------------------"

# Search for all JAR files
$Jars = Get-ChildItem -Path $ModuleDir -Recurse -Include *.xml | Sort-Object FullName
$Count = 0

foreach ($Jar in $Jars) {
    $Count++
    Write-Host "$Count. $($Jar.FullName)"
}

Write-Host "------------------------------------"
Write-Host "Total JAR files found: $Count"

# Save results to a file
$OutputFile = "u8cloud_xmls.txt"
$Jars.FullName | Out-File -FilePath $OutputFile -Encoding utf8
Write-Host "JAR list saved to: $OutputFile"

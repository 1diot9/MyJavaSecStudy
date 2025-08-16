param(
    [string]$JarListFile = "u8cloud_jars.txt",
    [string]$LibraryName = "U8CloudLibs",
    [string]$OutputDir = ".idea/libraries"
)

if (-not (Test-Path $JarListFile)) {
    Write-Host "Jar list file not found: $JarListFile"
    exit
}

# Make sure output directory exists
if (-not (Test-Path $OutputDir)) {
    New-Item -ItemType Directory -Force -Path $OutputDir | Out-Null
}

# Read all jar paths
$Jars = Get-Content $JarListFile | Where-Object { $_ -match "\.jar$" }

# Start XML content
$xmlContent = @()
$xmlContent += '<?xml version="1.0" encoding="UTF-8"?>'
$xmlContent += '<component name="libraryTable">'
$xmlContent += "  <library name=""$LibraryName"">"
$xmlContent += "    <CLASSES>"

foreach ($jar in $Jars) {
    $jarPath = $jar -replace "\\", "/" # Convert backslashes to forward slashes
    $xmlContent += "      <root url=""jar://$jarPath!/"" />"
}

$xmlContent += "    </CLASSES>"
$xmlContent += "    <JAVADOC />"
$xmlContent += "    <SOURCES />"
$xmlContent += "  </library>"
$xmlContent += "</component>"

# Output file
$outputFile = Join-Path $OutputDir "$LibraryName.xml"
$xmlContent | Out-File -FilePath $outputFile -Encoding utf8

Write-Host "Library XML generated: $outputFile"
Write-Host "Total jars added: $($Jars.Count)"

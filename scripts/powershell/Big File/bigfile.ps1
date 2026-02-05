param($FileName)
$Limit = 1MB

if (Test-Path $FileName){
    $Size = (Get-Item $FileName).Length
    if ($Size -gt $Limit) {
        Write-Host "Warning: File is too large"
    } else {
        Write-Host "File size is within limits"
    }
} else {
    Write-Host "File does not exist"
}
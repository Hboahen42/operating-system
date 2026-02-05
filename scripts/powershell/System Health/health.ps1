Write-Host "Date & Time:" (Get-Date)
Write-Host "Hostname:" $env:COMPUTERNAME
Write-Host "user:" $env:USERNAME
$disk = Get-PSDrive C
Write-Host "Total: $([math]::Round($disk.Used/1GB + $disk.Free/1GB, 2))GB Free: $([math]::Round($disk.Free/1GB,2))GB"
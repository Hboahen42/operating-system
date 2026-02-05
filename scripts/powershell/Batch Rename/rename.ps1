Get-ChildItem *.txt | ForEach-Object {
    Rename-Item $_.Name -NewName ("OLD_" + $_.Name)
    Write-Host "Renamed $($_.Name) to OLD_$($_.Name)"
}
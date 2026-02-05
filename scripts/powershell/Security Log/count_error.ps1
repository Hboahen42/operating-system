$Count = (Select-String "Error" server.log).Count
Write-Host "The string 'Error' appears in $Count lines"
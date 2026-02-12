$env:JAVA_HOME = "C:\Users\m.frank\scoop\apps\temurin25-jdk\current"
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"
Set-Location "C:\Users\m.frank\IdeaProjects\privat\captain-mercer\backend"
& .\mvnw.cmd clean package -q
exit $LASTEXITCODE

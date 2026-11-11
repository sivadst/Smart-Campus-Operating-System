@REM Maven Wrapper Batch script
@REM
@REM Copyright (c) 2017-2024 The Apache Software Foundation.
@REM Licensed under the Apache License, Version 2.0
@REM (the "License"); you may not use this file except in compliance with
@REM the License. You may obtain a copy of the License at
@REM
@REM     https://www.apache.org/licenses/LICENSE-2.0
@REM
@REM Unless required by applicable law or agreed to in writing, software
@REM distributed under the License is distributed on an "AS IS" BASIS,
@REM WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@REM See the License for the specific language governing permissions and
@REM limitations under the License.

@echo off

setlocal enableextensions enabledelayedexpansion

set "APP_HOME=%~dp0"
set "APP_HOME=%APP_HOME:\=/%"
set "APP_HOME=%APP_HOME:~0,-1%"

set "MAVEN_OPTS=%MAVEN_OPTS% -Dmaven.multiModuleProjectDirectory=%APP_HOME%"

if not exist "%APP_HOME%\.mvn\wrapper\maven-wrapper.jar" (
    echo Downloading maven-wrapper.jar...
    powershell -Command "Invoke-WebRequest -Uri 'https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar' -OutFile '%APP_HOME%\.mvn\wrapper\maven-wrapper.jar'"
)

java %MAVEN_OPTS% -classpath "%APP_HOME%\.mvn\wrapper\maven-wrapper.jar" -Dmaven.home="%APP_HOME%" -Dmaven.multiModuleProjectDirectory="%APP_HOME%" org.apache.maven.wrapper.MavenWrapperMain %*

Demo repository for standard tooling and CI around FASTEN projects.

### Repository Structure:

#### Directories:
- `.github/workflows` - contains the GitHub workflows for running model-ckecking in CI  
- `.mps` - FASTEN project configuration
- `build` - directory where FASTEN will be automatically downloaded by the scripts (initial empty)
- `gradle` - contains the gradle wrapper 
- `solutions` - contains the FASTEN solutions and models

#### Files:
- `build.gradle.kts, settings.gradle.kts` - Gradle scripts for resolving FASTEN and starting the model-checker headless
- `gradlew, gradlew.bat` - shell scripts for starting Gradle 
- `.gitattributes, .gitignore` - standard git configuration files
- `gradle.properties.example` -
- `startFasten.bat` - batch script to resolve and start the FASTEN IDE


### Usage:

Copy and rename `gradle.properties.example` to `gradle.properties`. Set inside newly copied 'gradle.properties' the needed information (github user and a token with rights to read packages).

#### Windows:
Start FASTEN:
```
fasten.bat
```

To check models run
```
gradlew.bat
```

#### Linux:
Start FASTEN:
```
./gradlew resolveMPS
cd build/fasten/bin
./fasten.sh
```

To check models run
```
gradlew
```

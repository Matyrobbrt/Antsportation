ModsDotGroovy.make {
    modLoader = 'javafml'
    loaderVersion = '[40,)'
    license = 'MIT'

    issueTrackerUrl = 'https://github.com/Matyrobbrt/Antsportation/issues'

    mod {
        modId = 'antsportation'
        version = this.version
        displayName = 'Antsportation'
        description = 'An item transportation mod. Now with ants!'
        authors = [
                'Matyrobbrt', 'ItsKillerLuc', 'Learwin'
        ]
        logoFile = 'antsportation.png'
        dependencies {
            forge = "[${this.forgeVersion},)"
            minecraft = this.minecraftVersionRange

            final modDependency = { String name ->
                mod(name) {
                    mandatory = false
                    versionRange = "[${this.buildProperties[name + '_version']},)"
                }
            }
            modDependency('jei')
            modDependency('patchouli')
            modDependency('theoneprobe')
        }
    }
}
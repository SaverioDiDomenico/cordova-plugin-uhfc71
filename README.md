# Cordova/Phonegap Plugin to manage RFID UHF Inventory on Handheld C71 Chainway cordova-plugin-uhfc71

Whit this plugin it's possible make inventory of RFID UHF Tags or make a search of specific tag.
It's also possible to set power of antenna before scanning.

## Supported Platforms
* Android (only)

## Installing

### Cordova

    $ cordova plugin add https://github.com/SaverioDiDomenico/cordova-plugin-uhfc71

### PhoneGap

    $ phonegap plugin add https://github.com/SaverioDiDomenico/cordova-plugin-uhfc71
    
### Notes    
    Check that the files with the .so extension, contained in the "src" folder,
    are copied to your project folder
    platforms \ android \ app \ src \ main \ jniLibs \ armeabi \
    otherwise copy them by hand


## Uninstall

### Cordova

    $ cordova plugin remove cordova-plugin-uhfc71

### PhoneGap

    $ phonegap plugin remove cordova-plugin-uhfc71
    
# API

## Methods

- inizializzazione
- scan

## scan

Make Inventory of Tags.

    var epcUHF = ''; //EMPTY if you  want to make full Inventory, in this case Scan Function return a String with all readed tags: For Example TAG1,TAG2,TAG3,
    //var epcUHF = 'TAG1'; //EPC of Tag that you are searching, in this case function return OK (tag is discovered) or KO (tag is not discovered)
    var waittimeUHF = 5000; //Scan duration in milliseconds (maximum 20000)
    var powerUHF = 10; //Possible values from 5 to 30
    
    var uhfc71 = new Uhfc71();
    
    //Initial Initialization
    uhfc71.inizializzazione(
			epcUHF, waittimeUHF, powerUHF,
			function(msg) {},
			function(err) {
				console.log(err);
			}
	);
    
    uhfc71.scan(
                epcUHF, waittimeUHF, powerUHF,
                function(msg) {
                    console.log(msg); //in msg you can see result of scan NO-TAGS / LIST / OK / KO
                },
                function(err) {
                    console.log(err);
                }
            );


# Credits

Company: 
Dynamic ID Srl - Web: www.dynamic-id.it - Email: info@dynamic-id.it

Developers: 
Saverio Di Domenico (didomenico@dynamic-id.it) - Luigi Casiello (casiello@dynamic-id.it)




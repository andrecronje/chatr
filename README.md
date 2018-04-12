## CHATR: Serverless Chat

Chatr uses the Babble SDK to enable multiple mobile devices to chat without a  
centralised server.

**WORK IN PROGRESS**

The SDK is from the *martin_mobile* branch... not *mobile*. 

## Test

1) Copy configuration file to emulated device:

```
adb push demo/mobile_node/ConfigBabbleMobile.json /storage/emulated/0/Android/data/io.mosaicnetworks.chatr/files/
```

2) iEstablish port forwarding on emulator:

```
adb forward tcp:6666 tcp:6666
``` 
3) Start mobile node by clicking the green triangle in Android Studio

4) Start the desktop node by running:

```
babble run --datadir=$PWD/demo/desktop_node/datadir --store=inmem --noclient --node_addr=10.0.0.8:1337
```

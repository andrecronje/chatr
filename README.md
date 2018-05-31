## CHATR: Serverless Chat

Chatr uses the Babble SDK to enable multiple mobile devices to chat without a  
centralised server.

**WORK IN PROGRESS**

The SDK is from the *martin_mobile* branch... not *mobile*. 

## Test

-1) Add directories to path

```
export PATH=$PATH:~/Android/Sdk/platform-tools
export PATH=$PATH:~/Android/Sdk/emulator
export PATH=$PATH:~/go/src/github.com/babbleio/babble/build
```

0) Start emulator

```
emulator -use-system-libs -avd Nexus_5X_API_P
```

1) Copy configuration file to emulated device:

```
adb push demo/mobile_node/ConfigBabbleMobile.json /storage/emulated/0/Android/data/io.mosaicnetworks.chatr/files/ConfigBabbleMobile.json
```

2) Establish port forwarding on emulator:

```
adb forward tcp:6666 tcp:6666
``` 
3) Start mobile node by clicking the green triangle in Android Studio

4) Start the desktop node by running:

```
babble run --datadir=$PWD/demo/desktop_node/datadir --store=inmem --node_addr=10.0.0.6:1337 --proxy_addr=10.0.0.6:1338 --client_addr=10.0.0.6:1339
```

5) Start Python application to act as desktop client

```
sendmessage --nodehost 10.0.0.6 --nodeport 1338 --listenhost 10.0.0.6 --listenport 1339
```

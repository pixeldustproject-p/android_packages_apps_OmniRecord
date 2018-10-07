About
-----

**OmniRecord** is what happens when you get tired of cherry picking
he same things over an over again in every new Android release

Start screenrecord via broadcast
```java

    final Intent intent = new Intent("org.omnirom.omnirecord.ACTION_START");
    intent.setPackage("org.omnirom.omnirecord");
    mContext.sendBroadcastAsUser(intent, UserHandle.CURRENT_USER);
```
    

Start record settings intent
```java
    new Intent(Intent.ACTION_MAIN).setClassName("org.omnirom.omnirecord",
                "org.omnirom.omnirecord.SettingsActivity");
```

Custom QS tile that can be enabled in manifest

License
-------

**OmniRecord** is licensed under the terms of the *GNU General Public License,
version 3.0*. See the *COPYING* file for the full license text.


-EOF-

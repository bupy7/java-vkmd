HOW TO CREATE ICNS
-------------------

Sizes: http://iconhandbook.co.uk/reference/chart/osx/

Create folder name `app.iconset`.

Put in the icons like below:

```
icon_1024x1024.png
icon_512x512.png
icon_512x512@2x.png
icon_256x256.png
icon_256x256@2x.png
icon_128x128.png
icon_128x128@2x.png
icon_32x32.png
icon_32x32@2x.png
icon_16x16.png	
icon_16x16@2x.png
```

Execute command (Only for macOS):

```
$ iconutil -c icns app.iconset
```

Profit!

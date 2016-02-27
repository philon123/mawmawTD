Dim bmpFontImages(2)
bmpFontImages(0)= LoadAnimImage("images/bmpFont60.png", 60, 60, 0, 40)
bmpFontImages(1)= LoadAnimImage("images/bmpFont35.png", 35, 35, 0, 40)
bmpFontImages(2)= LoadAnimImage("images/bmpFont20.png", 20, 20, 0, 40)

MaskImage bmpFontImages(0), 255, 255, 255
MaskImage bmpFontImages(1), 255, 255, 255
MaskImage bmpFontImages(2), 255, 255, 255

Function textBMP(xPos%, yPos%, textToWrite$, textSize#)

	Local scale%=0
	
	Select textSize
		Case 20 scale=2
		Case 35 scale=1
		Case 60 scale=0
		Default RuntimeError("Font size not supported! (Only 20, 35, 60)")
	End Select
	
	For i=1 To Len(textToWrite)
		Select Lower(Mid(textToWrite, i, 1))
			Case "a" DrawImage bmpFontImages(scale), xPos, yPos,  0 xPos = xPos + (35*(textSize/64))
			Case "b" DrawImage bmpFontImages(scale), xPos, yPos,  1 xPos = xPos + (32*(textSize/64))
			Case "c" DrawImage bmpFontImages(scale), xPos, yPos,  2 xPos = xPos + (30*(textSize/64))
			Case "d" DrawImage bmpFontImages(scale), xPos, yPos,  3 xPos = xPos + (32*(textSize/64))
			Case "e" DrawImage bmpFontImages(scale), xPos, yPos,  4 xPos = xPos + (33*(textSize/64))
			Case "f" DrawImage bmpFontImages(scale), xPos, yPos,  5 xPos = xPos + (22*(textSize/64))
			Case "g" DrawImage bmpFontImages(scale), xPos, yPos,  6 xPos = xPos + (30*(textSize/64))
			Case "h" DrawImage bmpFontImages(scale), xPos, yPos,  7 xPos = xPos + (30*(textSize/64))
			Case "i" DrawImage bmpFontImages(scale), xPos, yPos,  8 xPos = xPos + (10*(textSize/64))
			Case "j" DrawImage bmpFontImages(scale), xPos, yPos,  9 xPos = xPos + (14*(textSize/64))
			Case "k" DrawImage bmpFontImages(scale), xPos, yPos, 10 xPos = xPos + (29*(textSize/64))
			Case "l" DrawImage bmpFontImages(scale), xPos, yPos, 11 xPos = xPos + (10*(textSize/64))
			Case "m" DrawImage bmpFontImages(scale), xPos, yPos, 12 xPos = xPos + (48*(textSize/64))
			Case "n" DrawImage bmpFontImages(scale), xPos, yPos, 13 xPos = xPos + (30*(textSize/64))
			Case "o" DrawImage bmpFontImages(scale), xPos, yPos, 14 xPos = xPos + (32*(textSize/64))
			Case "p" DrawImage bmpFontImages(scale), xPos, yPos, 15 xPos = xPos + (32*(textSize/64))
			Case "q" DrawImage bmpFontImages(scale), xPos, yPos, 16 xPos = xPos + (33*(textSize/64))
			Case "r" DrawImage bmpFontImages(scale), xPos, yPos, 17 xPos = xPos + (20*(textSize/64))
			Case "s" DrawImage bmpFontImages(scale), xPos, yPos, 18 xPos = xPos + (28*(textSize/64))
			Case "t" DrawImage bmpFontImages(scale), xPos, yPos, 19 xPos = xPos + (18*(textSize/64))
			Case "u" DrawImage bmpFontImages(scale), xPos, yPos, 20 xPos = xPos + (30*(textSize/64))
			Case "v" DrawImage bmpFontImages(scale), xPos, yPos, 21 xPos = xPos + (33*(textSize/64))
			Case "w" DrawImage bmpFontImages(scale), xPos, yPos, 22 xPos = xPos + (46*(textSize/64))
			Case "x" DrawImage bmpFontImages(scale), xPos, yPos, 23 xPos = xPos + (30*(textSize/64))
			Case "y" DrawImage bmpFontImages(scale), xPos, yPos, 24 xPos = xPos + (30*(textSize/64))
			Case "z" DrawImage bmpFontImages(scale), xPos, yPos, 25 xPos = xPos + (33*(textSize/64))
			Case "." DrawImage bmpFontImages(scale), xPos, yPos, 26 xPos = xPos + (10*(textSize/64))
			Case "," DrawImage bmpFontImages(scale), xPos, yPos, 27 xPos = xPos + (10*(textSize/64))
			Case "!" DrawImage bmpFontImages(scale), xPos, yPos, 28 xPos = xPos + (15*(textSize/64))
			Case "?" DrawImage bmpFontImages(scale), xPos, yPos, 29 xPos = xPos + (30*(textSize/64))
			Case "1" DrawImage bmpFontImages(scale), xPos, yPos, 30 xPos = xPos + (20*(textSize/64))
			Case "2" DrawImage bmpFontImages(scale), xPos, yPos, 31 xPos = xPos + (33*(textSize/64))
			Case "3" DrawImage bmpFontImages(scale), xPos, yPos, 32 xPos = xPos + (32*(textSize/64))
			Case "4" DrawImage bmpFontImages(scale), xPos, yPos, 33 xPos = xPos + (35*(textSize/64))
			Case "5" DrawImage bmpFontImages(scale), xPos, yPos, 34 xPos = xPos + (32*(textSize/64))
			Case "6" DrawImage bmpFontImages(scale), xPos, yPos, 35 xPos = xPos + (33*(textSize/64))
			Case "7" DrawImage bmpFontImages(scale), xPos, yPos, 36 xPos = xPos + (32*(textSize/64))
			Case "8" DrawImage bmpFontImages(scale), xPos, yPos, 37 xPos = xPos + (33*(textSize/64))
			Case "9" DrawImage bmpFontImages(scale), xPos, yPos, 38 xPos = xPos + (32*(textSize/64))
			Case "0" DrawImage bmpFontImages(scale), xPos, yPos, 39 xPos = xPos + (33*(textSize/64))
			Default xPos = xPos + (25*(textSize/64))
			.skipSelect
		End Select
	Next
End Function
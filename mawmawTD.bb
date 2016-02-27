AppTitle "mawmaw TD"

;############################# GLOBALS
Const GAMEWIDTH%= 1024
Const GAMEHEIGHT%=768 

Global mx%, my%
Global m1Hit%, m1Down%
Global m2Hit%, m2Down%

Global levelDelay%=60*10 ;how long  between levels (in frames)
Global slowedEffectDur=60*5 ;how long is enemie slowed (in frames)

Global currLevel%=-1
Global currTrack%=2
Global numEnemies%
Global timeToNextLevel%=60*5

Global selectedTowerType%
Global objSelectedTower.tower
Global objRadius.object2d

Global plrLives%=69
Global plrMoney%=1000000
Global plrScore%=0



Graphics3D GAMEWIDTH, GAMEHEIGHT, 16, 2

Include "dataTowers.bb"
Include "dataTracks.bb"
Include "dataLevels.bb"
Include "bmpFontLib.bb"

;############################# LOAD

;-------Landscape
Dim texLandscape(10)
texLandscape(0)=LoadTexture("images/lndGras.bmp",1)
texLandscape(1)=LoadTexture("images/lndSt1.bmp", 1) ;vertical
texLandscape(2)=LoadTexture("images/lndSt2.bmp", 1) ;horizontal
texLandscape(3)=LoadTexture("images/lndSt3.bmp", 1) ;top to right
texLandscape(4)=LoadTexture("images/lndSt4.bmp", 1) ;right to bottom
texLandscape(5)=LoadTexture("images/lndSt5.bmp", 1) ;bottom to left
texLandscape(6)=LoadTexture("images/lndSt6.bmp", 1) ;left to top
texLandscape(7)=LoadTexture("images/lndSt7.bmp", 1) ;crossing
texLandscape(8)=LoadTexture("images/lndTree.bmp",1)

Dim objGameBoard.object2d(15,11)
For x=0 To 15
	For y=0 To 11
		createObject(x*64+32, y*64+32, 64, 64, texLandscape(gameboard(x,y,0,currTrack)), 2, 1)
		objGameBoard(x,y)=Last object2d
	Next
Next
texWhite=CreateTexture(1,1,3)
SetBuffer TextureBuffer(texWhite)
	ClsColor 255,255,255
	Cls
SetBuffer BackBuffer()

For x=0 To 15
	createObject(x*64, GAMEHEIGHT/2, 1, GAMEHEIGHT, texWhite, 1, 1)
Next
For y=0 To 11
	createObject(GAMEWIDTH/2, y*64, GAMEWIDTH, 1, texWhite, 1, 1)
Next

;-------Enemies
Dim texEnemies(2)
texEnemies(0)=LoadTexture("images/ene1.png",3)
texEnemies(1)=LoadTexture("images/ene2.png",3)
texEnemies(2)=LoadTexture("images/ene3.png",3)

;-------Towers
Dim texTowers(3)
texTowers(0)=LoadTexture("images/twr1.png",259)
texTowers(1)=LoadTexture("images/twr2.png",3)
texTowers(2)=LoadTexture("images/twr3.png",3)
texTowers(3)=LoadTexture("images/twr4.png",3)

;-------Shots
Dim texShots(3)
texShots(0)=LoadTexture("images/Sht1.png",3)
texShots(1)=LoadTexture("images/Sht2.png",3)
texShots(2)=LoadTexture("images/Sht3.png",3)
texShots(3)=LoadTexture("images/Sht4.png",3)

;-------Infobar
infoBarTex=LoadTexture("images/infoBar.png",51)
createObject(GAMEWIDTH/2,GAMEHEIGHT-32, GAMEWIDTH,64, infoBarTex, -1000, 1)

;coin
texCoin=LoadTexture("images/coin.png",3)
createObject(GAMEWIDTH-130, GAMEHEIGHT-16, 32, 32, texCoin, -1001, 1)

;heart
texHeart=LoadTexture("images/heart.png",3)
createObject(GAMEWIDTH-130, GAMEHEIGHT-48, 32, 32, texHeart, -1001, 1)

;numbers
Dim texDigits(9)
For i=0 To 9
	texDigits(i)=LoadAnimTexture("images/numbers.png",3,64,64,i,1)
Next

;upgrade arrow
Global texUpgArrow=LoadTexture("images/upgradeArrow.bmp",13)
Global upgArrow.object2d = createObject(GAMEWIDTH-580, GAMEHEIGHT-32, 32, 64, texUpgArrow, -1001, 1)

;towerinfo
Dim objInfoTower.object2d(3)
For i=0 To 3
	objInfoTower(i)=createObject(310 + i*147, GAMEHEIGHT-32, 64, 64, texTowers(i), -1001, 1)
Next

;-------Other
arial20=LoadFont("Arial", 20)
arial35=LoadFont("Arial", 35)
arial60=LoadFont("Arial", 60)

texRadius=LoadTexture("images/radius.png",51)
objRadius=createObject(-1,-1,1,1, texRadius, -1000, 0)

Global texSplash = LoadTexture("images/splash.png",51)
Global texIceSplash = LoadTexture("images/iceSplash.png",51)

EntityColor objRadius\sprite, 255, 255, 255

Global texNothing=CreateTexture(1,1,3)
WritePixel 0,0,$FF0000, TextureBuffer(texNothing)

Dim texUpgrade(63)
For i=0 To 63
	texUpgrade(i) = CreateTexture(64,64,3)
	SetBuffer TextureBuffer(texUpgrade(i))
		Color 0,0,0
		Rect 0,0,64,64
		Color 255,255,255
		Rect 0,0,64,i
Next
SetBuffer BackBuffer()

;-------Types
Type object2D
	Field enemy.enemy
	Field tower.tower
	Field shot.shot
	
	Field xPos#=0
	Field yPos#=0
	Field rotation%=0
	
	Field isStatic%=0
	
	Field texture
	Field inAnimation%=0
	Field currSize%=0
	Field endSize%=0
	Field scaleStep#=0
	
	Field sprite
End Type

Type enemy
	Field life#
	Field speed%
	Field walked%
	Field slowed%
	Field poisoned%
	Field poisonDamage#
	Field poisonDuration%
	Field nextTurn%
	Field tType%
	Field level%
	Field money%
	Field lifebar
	
	Field super.object2d
End Type

Type tower
	Field shotReady%
	Field level%
	Field tType%
	
	Field upgradeTimer%
	Field upgradeTimerMax%
	Field upgradeSprite
	
	Field super.object2d
End Type

Type shot
	Field goalX%
	Field goalY%
	Field speedX%
	Field speedY%
	Field comeFrom.tower
	Field rotation%
	Field tType%
	
	Field super.object2d
End Type


camera=CreateCamera()
CameraClsMode camera, 0, 1

;CameraProjMode camera, 2
;CameraZoom camera, 0.002

nextLevel()
frametimer=CreateTimer(50)
;############################# Main Loop
While Not KeyHit(1)
	Cls 
	;Stop
	WaitTimer frametimer
	
	m1Hit=MouseHit(1)
	m1Down=MouseDown(1)
	m2Hit=MouseHit(2)
	m2Down=MouseDown(2)
	mx=MouseX()
	my=MouseY()
	
	
	keyHits()
	updateEnemies()
	updateTowers()
	updateShots()
	drawAll()
	
	
	If plrLives<1 gameOver()
	
	timeToNextLevel=timeToNextLevel-1
	If timeToNextLevel<1 nextLevel()
	
	UpdateWorld
	RenderWorld
	
	Color 0,0,0
	timStart=MilliSecs()
	
	
	SetFont arial60
	Text 60, GAMEHEIGHT-60, Str(plrScore), 60
	
	SetFont arial35
	Text 930, GAMEHEIGHT-64, Str(plrLives), 35
	Text 935, GAMEHEIGHT-32, Str(plrMoney), 35
	
	SetFont arial20
	
	If objSelectedTower=Null Then
		;general information on towers
		
		For i=0 To 3
			
			Color 0,0,0
			If i=selectedTowerType Color 255,255,255
			
			Text 333+(i*147), GAMEHEIGHT-60, "Damg:" + Int(towerData(i,0,2))
			Text 333+(i*147), GAMEHEIGHT-40, "Range:" + Int(towerData(i,0,0))
			Text 333+(i*147), GAMEHEIGHT-20, "Reload:" + Int(towerData(i,0,5))
			
			Rect 283+(i*147), GAMEHEIGHT-63, 144, 62, 0
			Rect 284+(i*147), GAMEHEIGHT-64, 144, 62, 0
			
		Next
		
	Else
		;specified information on selected tower
		Text 333, GAMEHEIGHT-60, "Damage:" + Int(towerData(objSelectedTower\tType, objSelectedTower\level, 2))
		Text 333, GAMEHEIGHT-40, "Range:" + Int(towerData(objSelectedTower\tType, objSelectedTower\level, 0))
		Text 333, GAMEHEIGHT-20, "Speed:" + Int(towerData(objSelectedTower\tType, objSelectedTower\level, 5))
	EndIf
	Flip
Wend
End

;############################# FUNCTIONS

Function keyHits()
	If m1hit=1 And my>GAMEHEIGHT-64 Then
		For i=0 To 3
			If mx>283+(i*147) And mx<427+(i*147) Then
				selectedTowerType = i
			EndIf
		Next
	
	ElseIf m1hit=1 And gameBoard(mx/64, my/64,0,currTrack)=0 And gameBoard(mx/64, my/64,1,currTrack)=0 Then
		createTower(Floor(mx/64)*64, Floor(my/64)*64, selectedTowerType)
	EndIf
	
	If m1hit=1 And gameBoard(mx/64, my/64, 1,currTrack)=1 Then
		For t.tower=Each tower
			If t\super\xPos=(mx/64)*64 And t\super\yPos=(my/64)*64 Exit
		Next
		
		objSelectedTower=t.tower
		objRadius\xPos=objSelectedTower\super\xPos+32
		objRadius\yPos=objSelectedTower\super\yPos+32
	EndIf
	
	If KeyHit(28) Then
		selectedTowerType=(selectedTowerType+1) Mod 4
	EndIf
	
	If m2Hit=1 Then
		objSelectedTower=Null
	EndIf
	
	If KeyHit(200)=1 Then
		If objSelectedTower<>Null Then
			If objSelectedTower\level<5 Then
				upgradeTower(objSelectedTower)
			EndIf
		EndIf
	EndIf
	
	If KeyHit(25) pauseGame()
	
End Function

;############

Function drawAll()
	DrawImage bmpFontImages(0), 0, 0, 0
	sortObjects()
	
	If objSelectedTower<>Null Then
		EntityTexture objInfoTower(0)\sprite, texTowers(objSelectedTower\tType)	
		resizeSprite (objRadius\sprite,towerData(objSelectedTower\tType,objSelectedTower\level,0)*2,towerData(objSelectedTower\tType,objSelectedTower\level,0)*2)
		
		For i=1 To 3
			EntityTexture objInfoTower(i)\sprite, texNothing
		Next
		
		EntityTexture upgArrow\sprite, texUpgArrow
		
	Else
		For i=0 To 3
			EntityTexture objInfoTower(i)\sprite, texTowers(i)
		Next
		
		EntityTexture upgArrow\sprite, texNothing
		
		resizeSprite(objRadius\sprite,1,1)
	EndIf
	
	For obj.object2d=Each object2d
		If obj\isStatic=0 Then
			positionSprite(obj\sprite, obj\xPos, obj\yPos)
			
			If obj\enemy<>Null Then
				positionSprite(obj\enemy\lifebar, obj\xPos+32, obj\yPos)
				
				maxLife#=levelData(obj\enemy\level,5)
				resizeSprite(obj\enemy\lifebar, (obj\enemy\life*64)/maxLife , 8)
				EntityColor obj\enemy\lifebar, 255-(obj\enemy\life/maxLife)*255, (obj\enemy\life/maxLife)*255, 10
			EndIf
			
			
		EndIf
		
		If obj\tower<>Null Then
			If obj\tower\upgradeTimer>0 Then
				EntityTexture obj\tower\upgradeSprite, texUpgrade((Float(obj\tower\upgradeTimer)/obj\tower\upgradeTimerMax)*63)
				obj\tower\upgradeTimer = obj\tower\upgradeTimer-1

				If obj\tower\upgradeTimer=0 FreeEntity obj\tower\upgradeSprite : obj\tower\upgradeTimerMax=0
			EndIf
		EndIf
			
		If obj\inAnimation = 1 Then
			obj\currSize = obj\currSize + obj\scaleStep
			If obj\currSize <= obj\endSize Then
				resizeSprite(obj\sprite, obj\currSize, obj\currSize)
			Else
				deleteObject(obj)
			EndIf
		EndIf
	Next	
	
End Function

;############
Function spawnEnemies(level)
	For i=0 To levelData(level,0)
	
		xPos%=0
		yPos%=0
		tType%=levelData(currLevel,1)
		speed%=levelData(currLevel,3)
		money%=levelData(currLevel,4)
		life%=levelData(currLevel,5)
		
		;spawn from up
		If  turns(0,3,currTrack)=1 Then
			xPos = turns(0,0,currTrack) * 64
			yPos = 0 - (i * levelData(level,2))
		;spawn from down
		ElseIf turns(0,3,currTrack)=-1 Then
			xPos = turns(0,0,currTrack)* 64
			yPos = GAMEHEIGHT + (i * levelData(level,2))
		;spawn from left
		ElseIf turns(0,2,currTrack)=1 Then
			xPos = 0 - (i * levelData(level,2))
			yPos = turns(0,1,currTrack) * 64
		;spawn from right
		ElseIf turns(0,2,currTrack)=-1 Then
			xPos = GAMEWIDTH + (i * levelData(level,2))
			yPos =turns(0,1,currTrack) * 64
		EndIf
		
		
		
		createEnemy(xPos,yPos,tType, speed, life, money, level)
		
	Next
End Function
;############

Function updateEnemies()
	numEnemies=0
	For e.enemy=Each enemy
		numEnemies=numEnemies+1
		
		;find speed
		If e\slowed>0 e\slowed=e\slowed-1
		speed#=levelData(e\level, 3)
		If e\slowed>0 speed=speed/2
		e\speed = speed
		
		;next turn reached?
		If e\nextTurn<numTurns(currTrack) And Abs(turns(e\nextTurn,0,currTrack)*64-e\super\xPos)<(speed+1) And Abs(turns(e\nextTurn,1,currTrack)*64-e\super\yPos)<(e\speed+1) Then
				e\super\xPos = turns(e\nextTurn,0,currTrack)*64
				e\super\yPos = turns(e\nextTurn,1,currTrack)*64
				e\nextTurn=e\nextTurn+1
			
		;enter map
		ElseIf e\nextTurn=0 Then	
			If    (turns(0,0,currTrack)*64 - e\super\xPos) < 0 Then
				e\super\xPos=e\super\xPos - e\speed
			ElseIf (turns(0,0,currTrack)*64 - e\super\xPos) > 0 Then
				e\super\xPos=e\super\xPos + e\speed 
			ElseIf (turns(0,1,currTrack)*64 - e\super\yPos) < 0 Then
				e\super\yPos=e\super\yPos - e\speed
			ElseIf (turns(0,1,currTrack)*64 - e\super\yPos) > 0 Then
				e\super\yPos=e\super\yPos + e\speed 
			EndIf
		
		;leave map
		ElseIf e\nextTurn=numTurns(currTrack) Then
			e\super\xPos = e\super\xPos + turns(numTurns(currTrack),2,currTrack)*e\speed
			e\super\yPos = e\super\yPos + turns(numTurns(currTrack),3,currTrack)*e\speed 
			
		
		;normal movement
		Else
			e\super\xPos = e\super\xPos + turns(e\nextTurn-1, 2, currTrack)*e\speed
			e\super\yPos = e\super\yPos + turns(e\nextTurn-1, 3, currTrack)*e\speed
		
		EndIf
		
		;is off map
		If e\super\xPos<-64 Or e\super\xPos>GAMEWIDTH Or e\super\yPos<-64 Or e\super\yPos>GAMEHEIGHT Then
			If e\nextTurn=numTurns(currTrack) Then
				deleteObject(e\super)
				plrLives=plrLives-1 ;loose life
			EndIf
		EndIf
		
		;is enemy dead?
		If e<>Null Then
			If e\poisoned=1 Then
				e\life = e\life - e\poisonDamage
				e\poisonDuration = e\poisonDuration -1
				If e\poisonDuration=0 e\poisoned=0
			EndIf
			
			If e\life<1 Then
				plrMoney=plrMoney+levelData(currLevel, 4)
				plrScore=plrScore+(e\level+1)*10
				deleteObject(e\super)
			EndIf
		EndIf 
	Next
	If numEnemies=0 And currLevel=numLevels gameWin()
End Function

;############

Function updateTowers()
	For t.tower=Each tower
		If t\shotReady<>0 t\shotReady=t\shotReady-1
		For e.enemy=Each enemy
			If Sqr((e\super\xPos-t\super\xPos)^2+(e\super\yPos-t\super\yPos)^2)<towerData(t\tType,t\level,0) Then
				If  Not e\super\xPos<-64 Or e\super\xPos>GAMEWIDTH Or e\super\yPos<-64 Or e\super\yPos>GAMEHEIGHT Then
					If t\upgradeTimer=0 Then
						If t\shotReady=0 Then
							t\shotReady=towerData(t\tType, t\level, 5)
							createShot(t\super\xPos+32, t\super\yPos+32, e\super\xPos+32, e\super\yPos+32, t.tower)
						EndIf
					EndIf
				EndIf
			EndIf
		Next
	Next
End Function 

;############

Function updateShots()
	For s.shot=Each shot
		
		s\super\xPos=s\super\xPos+s\speedX
		s\super\yPos=s\super\yPos+s\speedY
		
		;out of bounds
		If s\super\xPos<0 Or s\super\xPos>GAMEWIDTH Or s\super\yPos<0 Or s\super\yPos>GAMEHEIGHT Then
			
			deleteObject (s\super) 
		Else
			
			;hit:
			
			;direct 
			For e.enemy=Each enemy
				If RectsOverlap (e\super\xPos,e\super\yPos,64,64,s\super\xPos,s\super\yPos,1,1)=1 Then
					e\life=e\life-towerData(s\tType,s\comeFrom\level,2)
					
					If s\tType=2 e\slowed=slowedEffectDur
					
					If s\tType=3 Then
						e\poisoned=1
						e\poisonDamage=towerData(s\comeFrom\tType, s\comeFrom\level, 6)
						e\poisonDuration = towerData(s\comeFrom\tType, s\comeFrom\level, 7)
					EndIf
					
					
					;splash
					If s\tType=1 Or s\tType=2 Then
						If s\tType=1 createObject(e\super\xPos+32, e\super\yPos+32, 0,0, texSplash, -1000, 1)
						If s\tType=2 createObject(e\super\xPos+32, e\super\yPos+32, 0,0, texIceSplash, -1000, 1)
						obj.object2d=Last object2d
						resizeSprite(obj\sprite, 0,0)
						obj\currSize = 0
						obj\endSize = towerData(s\comeFrom\tType, s\comeFrom\level, 4)*2
						obj\scaleStep = (obj\endSize-obj\currSize)/20
						obj\inAnimation=1
						
						For en.enemy=Each enemy
							If en.enemy<>e.enemy Then
								If Sqr(Abs(e\super\xPos-en\super\xPos)^2+Abs(e\super\yPos-en\super\yPos)^2)<towerData(s\tType,s\comeFrom\level,4) Then
									en\life=en\life-towerData(s\tType,s\comeFrom\level,3)*2
									If s\tType=2 en\slowed=slowedEffectDur
								EndIf
							EndIf
						Next
					EndIf
					
					deleteObject(s\super)
					Exit
					
				EndIf
			Next
			
			
		EndIf
		
	Next
End Function

;############

Function positionSprite(sprite,x%,y%)
	PositionEntity sprite, x-GAMEWIDTH/2,-(y-GAMEHEIGHT/2),GAMEWIDTH/2
End Function
;############

Function resizeSprite(sprite, x#,y#)
	
	ScaleSprite sprite,x/2,y/2
	
End Function

;############

Function nextLevel()
	
	If currLevel<>numLevels Then
		currLevel=currLevel+1
		spawnEnemies(currLevel)
		timeToNextLevel=levelData(currLevel, 0) * levelData(currLevel, 2) / levelData(currLevel, 3) + levelDelay
	EndIf
	
End Function

;############

Function createTower(xPos%, yPos%, tType%)
	
	If plrMoney+1>towerData(tType, 0, 9) Then
		
		gameBoard(xPos/64,yPos/64,1,currTrack)=1
		
		obj.object2d=New object2d
		obj\xPos=xPos
		obj\yPos=yPos
		obj\isStatic=1
		
		obj\tower=New tower
		obj\tower\shotReady=1
		obj\tower\tType=selectedTowerType
		obj\tower\super=obj
		
		obj\sprite = CreateSprite()
		HandleSprite obj\sprite,-1,1
		positionSprite(obj\sprite,obj\xPos,obj\yPos)
		resizeSprite(obj\sprite,64,64)
		EntityTexture obj\sprite, texTowers(obj\tower\tType)
		
		
		plrMoney = plrMoney - towerData(tType, 0, 9)
		obj\tower\upgradeTimer=towerData(tType, 0, 8)
		obj\tower\upgradeTimerMax=towerData(tType, 0, 8)
		
		If towerData(tType, 0, 8)>0 Then
			obj\tower\upgradeSprite = CreateSprite()
			resizeSprite (obj\tower\upgradeSprite, 64,64)
			positionSprite(obj\tower\upgradeSprite, obj\xPos+32, obj\yPos+32)
			EntityOrder obj\tower\upgradeSprite, -1
			EntityAlpha obj\tower\upgradeSprite, 0.5
		EndIf
	EndIf
	
End Function

;############

Function createEnemy(xPos%, yPos%, tType%, speed%, life%, money%, level%)
	
	obj.object2d=New object2d
	obj\xPos=xPos
	obj\yPos=yPos
	obj\isStatic=0
	
	obj\enemy = New enemy
	obj\enemy\tType = tType
	obj\enemy\speed = speed
	obj\enemy\life  = life
	obj\enemy\money = money
	obj\enemy\nextTurn = 0
	obj\enemy\level = level
	obj\enemy\super = obj
	
	obj\sprite = CreateSprite()
	HandleSprite obj\sprite,-1,1
	positionSprite(obj\sprite,obj\xPos+32,obj\yPos+32)
	resizeSprite(obj\sprite,64,64)
	EntityTexture obj\sprite, texEnemies(obj\enemy\tType)
	
	obj\enemy\lifebar = CreateSprite()
	resizeSprite(obj\enemy\lifebar,64,8)
	EntityOrder obj\enemy\lifebar, -999
	
End Function

;############

Function createShot(startX, startY%, goalX%, goalY%, tt.tower)
	
	obj.object2d=New object2d
	obj\xPos=startX
	obj\yPos=startY
	obj\isStatic=0
	
	obj\shot = New shot
	obj\shot\goalX=goalX
	obj\shot\goalY=goalY
	obj\shot\comeFrom=tt
	obj\shot\tType=tt\tType
	obj\shot\super = obj
	
	winkel=ATan2((goalY-startY),(goalX-startX))
	obj\shot\speedX=Cos(winkel)*towerData(tt\tType,tt\level,1)
	obj\shot\speedY=Sin(winkel)*towerData(tt\tType,tt\level,1)
	
	winkel=(ATan2((obj\shot\goalY-obj\yPos), -(obj\shot\goalX-obj\xPos)) + 360) Mod 360 +180
	obj\rotation = winkel
	
	obj\sprite = CreateSprite()
	positionSprite(obj\sprite,obj\xPos,obj\yPos)
	resizeSprite(obj\sprite,64,64)
	ScaleSprite obj\sprite,10*(tt\level+1),5*(tt\level+1)
	EntityTexture obj\sprite, texShots(obj\shot\tType)
	RotateSprite obj\sprite, obj\rotation
		
End Function

;############

Function createObject.object2d(xPos%, yPos%, xSize%, ySize%, tex, order%, static%)
	
	obj.object2d=New object2d
	obj\xPos=xPos
	obj\yPos=yPos
	obj\isStatic=static
	
	obj\sprite = CreateSprite()
	positionSprite(obj\sprite,obj\xPos,obj\yPos)
	resizeSprite(obj\sprite,xSize,ySize)
	EntityTexture obj\sprite, tex
	EntityOrder obj\sprite, order
	
	Return obj
	
End Function

;############

Function deleteObject(obj.object2d)
	FreeEntity obj\sprite
	Delete obj\tower
	Delete obj\shot
	If obj\enemy<>Null Then
		FreeEntity obj\enemy\lifebar
	EndIf
	Delete obj\enemy
	FreeTexture obj\texture
	
	Delete obj
End Function

;############

Function sortObjects()
	
	For obj.object2d = Each object2d
		If obj\isStatic=0 EntityOrder obj\sprite, -obj\yPos : If obj\yPos<0 EntityOrder obj\sprite, -1
	Next
	
End Function

;############

Function gameOver()
	End
End Function

;############

Function gameWin()
	End
End Function

;############

Function pauseGame()
	pauseTimer=CreateTimer(60)
	
	While Not KeyHit(25) Or KeyHit(1)
		WaitTimer pauseTimer 
	Wend
	FreeTimer pauseTimer
End Function

;############

Function upgradeTower(t.tower)
	
	If plrMoney+1>towerData(t\tType, t\level, 9) Then
		If t\upgradeTimer=0 Then
			plrMoney = plrMoney - towerData(t\tType, t\level, 9)
			t\upgradeTimer=towerData(t\tType, t\level+1, 8)
			t\upgradeTimerMax=towerData(t\tType, t\level+1, 8)
			t\level = t\level + 1
			
			t\upgradeSprite = CreateSprite()
			resizeSprite (t\upgradeSprite, 64,64)
			positionSprite(t\upgradeSprite, t\super\xPos+32, t\super\yPos+32)
			EntityOrder t\upgradeSprite, -1
			EntityAlpha t\upgradeSprite, 0.5
		EndIf
	EndIf
	
End Function
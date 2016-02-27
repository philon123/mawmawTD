;#################
;Data for towers
;#################

numAtts=9

Dim towerData#(3,5,numAtts) ; towertype, level, attribute 

;(0)range, (1)bullet speed, (2)direct damage, (3)splash damage, (4)splash radius, (5)reload speed, (6)extra damage per frame (poison), (7)poison duration, (8)upgrade time (frames), (9)upgradecost

;Arrow
Data 150, 20,   20,  0,  0, 60, 0, 0,  60,  10  ;lvl1
Data 175, 20,   50,  0,  0, 60, 0, 0,  80,  20  ;lvl2
Data 200, 20,  120,  0,  0, 60, 0, 0, 100,  30  ;lvl3
Data 225, 20,  280,  0,  0, 60, 0, 0, 120,  60  ;lvl4
Data 250, 20,  620,  0,  0, 60, 0, 0, 140, 120  ;lvl5
Data 300, 20, 1500,  0,  0, 60, 0, 0, 160, 240  ;lvl6
For y=0 To 5
	For x=0 To numAtts
		Read towerData(0,y,x)
	Next
Next

;Canon
Data 100, 10,   30,  15,  70, 100, 0, 0, 100,  25
Data 120, 10,   60,  30,  80, 100, 0, 0, 140,  50
Data 140, 10,  125,  62,  90, 100, 0, 0, 180,  75
Data 160, 10,  250, 125, 100, 100, 0, 0, 220, 150
Data 180, 10,  500, 250, 110, 100, 0, 0, 260, 300
Data 200, 10, 1000 ,500, 120, 100, 0, 0, 300, 450
For y=0 To 5
	For x=0 To numAtts
		Read towerData(1,y,x)
	Next
Next

;Ice
Data 100, 20,  10,  5,  70, 80, 0, 0,  60, 50
Data 120, 20,  20, 10,  75, 80, 0, 0,  80, 50
Data 140, 20,  40, 20,  80, 80, 0, 0, 100, 50
Data 160, 20,  60, 30,  85, 80, 0, 0, 120, 50
Data 180, 20, 100, 50,  90, 80, 0, 0, 140, 50
Data 200, 20, 160, 80, 100, 80, 0, 0, 160, 50
For y=0 To 5
	For x=0 To numAtts
		Read towerData(2,y,x)
	Next
Next

;Poison
Data 300, 20,  5,  0, 50, 60, 0.3,120,  0, 0
Data   0,  0,  0,  0,  0, 60, 0  ,  0, 60, 0
Data   0,  0,  0,  0,  0, 60, 0  ,  0, 60, 0
Data   0,  0,  0,  0,  0, 60, 0  ,  0, 60, 0
Data   0,  0,  0,  0,  0, 60, 0  ,  0, 60, 0
Data   0,  0,  0,  0,  0, 60, 0  ,  0, 60, 0
For y=0 To 5
	For x=0 To numAtts
		Read towerData(3,y,x)
	Next
Next

 
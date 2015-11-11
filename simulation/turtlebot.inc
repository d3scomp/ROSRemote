define xtion ranger (
	sensor (
		range_max 6.5
		fov 58.0
		samples 640
	)
	color "black"
	size [ 0.06 0.15 0.03 ]
)

define bump bumper (
  # bumper properties
  bcount 1
  bpose[0] [ 0 0 0 ]
  blength 0.1
)

define turtlebot position (
	pose [ 0.0 0.0 0.0 0.0 ]

	odom_error [0.03 0.03 999999 999999 999999 0.02]

	# 6.3kg + laptop + xtion + equipment board
	mass 10

	size [ 0.354 0.354 0.420 ]
	origin [ 0.0 0.0 0.0 0.0 ]
	
	bitmap "turtlebot.png"

	gui_nose 1
	drive "diff"
	color "gray"

	xtion(pose [ -0.1 0.0 -0.11 0.0 ])
)

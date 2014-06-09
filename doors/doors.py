#!/usr/bin/python

import time
import os.path
import RPi.GPIO as io

io.setmode(io.BCM)

script_door1 = "/etc/automated/door1-script"
script_door2 = "/etc/automated/door2-script"
pir_pin = 18
door1_pin = 23
door2_pin = 24

io.setup(pir_pin, io.IN) # activate input
io.setup(door1_pin, io.IN, pull_up_down=io.PUD_UP) # activate input with PullUp
io.setup(door2_pin, io.IN, pull_up_down=io.PUD_UP) # activate input with PullUp

while True:
   print("LOOP!")
   if io.input(door1_pin):
      if (os.path.isfile (script_door1)):
         subprocess.call ([script_door1])
      else:
         print("DOOR1 ALARM - NO SCRIPT!")

   if io.input(door2_pin):
      if (os.path.isfile (script_door1)):
         subprocess.call ([script_door1])
      else:
         print("DOOR2 ALARM - NO SCRIPT!")

   time.sleep(0.5)

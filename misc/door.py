import time
import RPi.GPIO as io
io.setmode(io.BCM)

pir_pin = 18
door1_pin = 23
door2_pin = 24

io.setup(pir_pin, io.IN) # activate input
io.setup(door1_pin, io.IN, pull_up_down=io.PUD_UP) # activate input with PullUp
io.setup(door2_pin, io.IN, pull_up_down=io.PUD_UP) # activate input with PullUp

while True:
   if io.input(door1_pin):
      print("DOOR1 ALARM!")

   if io.input(door2_pin):
      print("DOOR2 ALARM!")
   time.sleep(0.5)

#TODO

##2013-04-09
- Make "arcane forge" require a construction recipe.
  - Could stay large, or become a single "special" block (with damage/data value or something)
  - "Special" block might have a particle effect
- Hunt for optimal amount of scrap to use
- Restrict AF to only enchanted items
- Restrict AF to only certain base materials
- DONE: Make sure (test!) Repair Cost is being respected
- repair and cost commands (will req. breakout of repair logic from event handler)
- permissions to repair
  - maybe permissions for minimum and maximum levels, material types, etc.?
- Can I show the block damage overlay permanently on the anvil as it breaks?
  - https://gist.github.com/aadnk/3788593
  - It would only have to send animation for a player that was nearby
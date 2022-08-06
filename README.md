# Reinforcement Learning

## Architecture

  1. **3 Input Neurons:**
     - The expected angle between the gun and the target.
     - The current angle the gun is pointing to.
     - The difference between the current angle and the expected angle ( _error_). 
     
``` kotlin
        val a1= (90f-atan2(target.getY()-getY(),target.getX()-getX())*180.0/Math.PI)
        val a2=(getRotationZ()+90f).toDouble()
        val a3=(a1-a2)
        
```
        
  2. **8 Hidden Neurons:**
  
  3. **1 Output Neuron:**
      - If output value is >=0.5f subtract the angle by some value else add the angle by some value.
        
``` kotlin
 when (if(output[0]>=0.5) 1 else 0) {
            0 -> 
                //subtract angle by 0.5f
              moveRay(0)
            1 -> 
                //add angle by 0.5f
              moveRay(1)
        }
```        
 
## ScreenShots

![crop1](https://user-images.githubusercontent.com/41951671/183241959-c384443a-1915-43cd-83be-8b45054afb61.png)

![crop2](https://user-images.githubusercontent.com/41951671/183241933-3552beb5-68d9-41ee-bab1-f51b1be8f806.png)

![crop3](https://user-images.githubusercontent.com/41951671/183241927-9dc4c501-bf00-4755-913b-28ea36d74db7.png)



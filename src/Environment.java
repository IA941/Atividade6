/*****************************************************************************
 * Copyright 2007-2015 DCA-FEEC-UNICAMP
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *    Klaus Raizer, Andre Paraense, Ricardo Ribeiro Gudwin
 *****************************************************************************/

import ws3dproxy.CommandExecException;
import ws3dproxy.WS3DProxy;
import ws3dproxy.model.Creature;
import ws3dproxy.model.World;

/**
 * @author rgudwin
 */
public class Environment {

    public Creature myCreature = null;

    public Environment() {
        WS3DProxy proxy = new WS3DProxy();
        try {
            World w = World.getInstance();
            w.reset();
            myCreature = proxy.createCreature(25, 25, 0);
            myCreature.start();
            System.out.println("Starting the WS3D Resource Generator ... ");
            //World.grow(1);
            Thread.sleep(4000);
            myCreature.updateState();
            World.createBrick(4,230.0,0.0,240.00,400.0);
            World.createBrick(4,370.0,200.0,380.00,600.00);
            World.createBrick(4,530.0,0.0,540.00,400.0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Robot " + myCreature.getName() + " is ready to go.");
    }
}

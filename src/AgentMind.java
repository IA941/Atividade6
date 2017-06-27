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

import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.MemoryObject;
import br.unicamp.cst.core.entities.Mind;
import codelets.behaviors.MotionCodelet;
import codelets.motor.LegsActionCodelet;
import codelets.perception.*;
import codelets.sensors.InnerSense;
import codelets.sensors.Vision;
import memory.CreatureInnerSense;
import model.GridMap;
import model.SimulationStatus;
import support.MindView;
import ws3dproxy.model.Leaflet;
import ws3dproxy.model.Thing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author rgudwin
 */
public class AgentMind extends Mind {

    public AgentMind(Environment env) {
        super();

        // Declare Memory Objects        
        MemoryObject legsMO;
        MemoryObject visionMO;
        MemoryObject innerSenseMO;
        MemoryObject knownWallsMO;

        //Initialize Memory Objects
        legsMO = createMemoryObject("LEGS", "");
        List<Thing> vision_list = Collections.synchronizedList(new ArrayList<Thing>());
        visionMO = createMemoryObject("VISION", vision_list);
        CreatureInnerSense cis = new CreatureInnerSense();
        innerSenseMO = createMemoryObject("INNER", cis);
        List<Thing> knownWalls = Collections.synchronizedList(new ArrayList<Thing>());
        knownWallsMO = createMemoryObject("KNOWN_WALLS", knownWalls);

        // Create and Populate MindViewer
        MindView mv = new MindView("MindView");
        mv.addMO(knownWallsMO);
        mv.addMO(visionMO);
        mv.addMO(innerSenseMO);
        mv.addMO(legsMO);

        mv.StartTimer();
        mv.setVisible(true);

        // Create Sensor Codelets
        Codelet vision = new Vision(env.myCreature);
        vision.addOutput(visionMO);
        insertCodelet(vision);

        Codelet innerSense = new InnerSense(env.myCreature);
        innerSense.addOutput(innerSenseMO);
        insertCodelet(innerSense);

        Codelet legs = new LegsActionCodelet(env.myCreature);
        legs.addInput(legsMO);
        insertCodelet(legs);

        final GridMap gridMap = new GridMap(25, 25, 775, 25);

        Codelet wallDetector = new WallDetector(gridMap);
        wallDetector.addInput(visionMO);
        wallDetector.addOutput(knownWallsMO);
        insertCodelet(wallDetector);

        Codelet motion = new MotionCodelet(gridMap, env.myCreature, this);
        motion.addOutput(legsMO);
        insertCodelet(motion);

        start();
    }
}

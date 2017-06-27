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

package codelets.perception;

import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.MemoryObject;
import model.GridMap;
import ws3dproxy.model.Thing;
import ws3dproxy.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class WallDetector extends Codelet {

    private final GridMap gridMap;
    private MemoryObject visionMO;
    private MemoryObject knownWalls;

    public WallDetector(final GridMap gridMap) {
        this.gridMap = gridMap;
    }

    @Override
    public void accessMemoryObjects() {
        synchronized (this) {
            this.visionMO = (MemoryObject) this.getInput("VISION");
        }
        this.knownWalls = (MemoryObject) this.getOutput("KNOWN_WALLS");
    }

    @Override
    public void proc() {
        List<Thing> vision;
        synchronized (visionMO) {
            vision = new ArrayList<>((List<Thing>) visionMO.getI());
        }

        List<Thing> knownWalls = (List<Thing>) this.knownWalls.getI();

        synchronized (knownWalls) {
            for (Thing thing : vision) {
                if (thing.getCategory() == Constants.categoryBRICK) {
                    if (knownWalls.isEmpty() || knownWalls.stream().noneMatch(wall -> wall.getName().equals(thing.getName()))) {
                        knownWalls.add(thing);
                        gridMap.markWall(thing);
                    }
                }
            }
        }
    }

    @Override
    public void calculateActivation() {

    }
}



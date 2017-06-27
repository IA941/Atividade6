package codelets.behaviors;

import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.MemoryObject;
import br.unicamp.cst.core.entities.Mind;
import model.Coordinate;
import model.GridMap;
import org.json.JSONException;
import org.json.JSONObject;
import ws3dproxy.model.Creature;

import java.util.List;

public class MotionCodelet extends Codelet {

    private final GridMap gridMap;
    private final Creature creature;
    private final Mind mind;
    private MemoryObject legsMO;


    public MotionCodelet(final GridMap gridMap, final Creature creature, final Mind mind) {
        setTimeStep(300);
        this.gridMap = gridMap;
        this.creature = creature;
        this.mind = mind;
    }


    @Override
    public void accessMemoryObjects() {
        legsMO = (MemoryObject) this.getOutput("LEGS");
    }

    @Override
    public void calculateActivation() {

    }

    @Override
    public void proc() {
        gridMap.markStartPosition(creature.getPosition().getX(), creature.getPosition().getY());
        JSONObject message = new JSONObject();
        try {
            List<Coordinate> coordinates = gridMap.findPath();
            if (coordinates != null && !coordinates.isEmpty()) {
                message.put("ACTION", "GOTO");
                message.put("X", (int) coordinates.get(0).getX());
                message.put("Y", (int) coordinates.get(0).getY());
                message.put("SPEED", 0.5);
                System.out.println("Motion.proc: " + message.toString());
                legsMO.setI(message.toString());
            } else {
                System.out.println("Ending simulation...");
                mind.shutDown();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

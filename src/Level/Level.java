package Level;

import Entities.Camera;
import Entities.Entity;
import Entities.Light;
import Entities.Player;
import RenderEngine.Loader;
import RenderEngine.MasterRenderer;
import Terrains.Terrain;
import Water.WaterTile;
import guis.GuiRenderer;
import guis.GuiTexture;
import org.lwjgl.util.vector.Vector3f;
import java.util.List;

public interface Level {

    // Needed entities
    Camera camera = null;
    Loader loader = null;;
    MasterRenderer renderer = null;;
    GuiRenderer guiRenderer = null;;

    // Level content
    List<Entity> mapEntities  = null;
    List<Light> lights = null;
    List<Player> players = null;
    List<GuiTexture> guis = null;
    List<Terrain> terrains = null;
    List<WaterTile> water = null;

    // Make it pretty
    Vector3f sunColour = new Vector3f(1.5f,1.5f,1f);
    Vector3f sunPosition = new Vector3f(1400,1500,1400);
    Vector3f FOG_COLOUR = new Vector3f(1.5f,1.5f,1f);

    void GenerateEntities();
    void LoadLevel();
    void EventTick();
    void RenderLevel();
    void RenderTerrains();
    void RenderEntities();
    void RenderWater();
    void RenderPlayers();
    void UpdatePlayers();
    void UpdateEntities();
    void UpdateCamera();
    void CleanUp();
}

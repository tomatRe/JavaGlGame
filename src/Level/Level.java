package Level;

import Entities.Camera;
import Entities.Entity;
import Entities.Light;
import Entities.Player;
import Models.RawModel;
import Models.TexturedModel;
import RenderEngine.DisplayManager;
import RenderEngine.Loader;
import RenderEngine.MasterRenderer;
import RenderEngine.ObjLoader;
import Terrains.Terrain;
import Textures.ModelTexture;
import Textures.TerrainTexture;
import Textures.TerrainTexturePack;
import guis.GuiRenderer;
import guis.GuiTexture;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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
    void RenderPlayers();
    void UpdatePlayers();
    void UpdateEntities();
    void UpdateCamera();
}

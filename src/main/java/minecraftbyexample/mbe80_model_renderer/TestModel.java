/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package minecraftbyexample.mbe80_model_renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import minecraftbyexample.usefultools.debugging.DebugSettings;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.Vec3i;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Class to help learn about Models and model rendering
 * Allows to interactively change model parameters in game:
 * * box origin, box size, box texture origin
 * * box rotation point
 * * box rotation angles
 * for parent model and child model
 *
 * Default is a 16x16x16 parent box centred at [0,0,0] with a 1x3x2 child box centred at [0, 16, 0]
 *
 */

public class TestModel extends Model {

  public ModelRenderer parentModel;
  public ModelRenderer childModel;

	public TestModel(InteractiveParameters interactiveParameters) {
		super(RenderType::getEntitySolid);

		ip = interactiveParameters;

		textureWidth = 64;
		textureHeight = 32;

    parentModel = new ModelRenderer(this, ip.PARENT_TEXTURE_ORIGIN.getX(), ip.PARENT_TEXTURE_ORIGIN.getY());
    parentModel.setRotationPoint(ip.PARENT_ROTATION_POINT.getX(), ip.PARENT_ROTATION_POINT.getY(), ip.PARENT_ROTATION_POINT.getZ());
    parentModel.addBox(ip.PARENT_CORNER.getX(), ip.PARENT_CORNER.getY(), ip.PARENT_CORNER.getZ(),
            ip.PARENT_BLOCK_DIMENSIONS.getX(), ip.PARENT_BLOCK_DIMENSIONS.getY(), ip.PARENT_BLOCK_DIMENSIONS.getZ(),
            ip.PARENT_DELTA.floatValue());

    childModel = new ModelRenderer(this, ip.CHILD_TEXTURE_ORIGIN.getX(), ip.CHILD_TEXTURE_ORIGIN.getY());
    childModel.setRotationPoint(ip.CHILD_ROTATION_POINT.getX(), ip.CHILD_ROTATION_POINT.getY(), ip.CHILD_ROTATION_POINT.getZ());
    childModel.addBox(ip.CHILD_CORNER.getX(), ip.CHILD_CORNER.getY(), ip.CHILD_CORNER.getZ(),
            ip.CHILD_BLOCK_DIMENSIONS.getX(), ip.CHILD_BLOCK_DIMENSIONS.getY(), ip.CHILD_BLOCK_DIMENSIONS.getZ(),
            ip.CHILD_DELTA.floatValue());
  
    parentModel.addChild(childModel);
  }

	@Override
	public void render(MatrixStack matrixStack, IVertexBuilder renderBuffer, int combinedLight, int combinedOverlay, float r, float g, float b, float a) {

    childModel.rotateAngleX = (float)Math.toRadians(ip.CHILD_ROTATION_ANGLE.getX());
    childModel.rotateAngleY = (float)Math.toRadians(ip.CHILD_ROTATION_ANGLE.getY());
    childModel.rotateAngleZ = (float)Math.toRadians(ip.CHILD_ROTATION_ANGLE.getZ());

    parentModel.rotateAngleX = (float)Math.toRadians(ip.PARENT_ROTATION_ANGLE.getX());
    parentModel.rotateAngleY = (float)Math.toRadians(ip.PARENT_ROTATION_ANGLE.getY());
    parentModel.rotateAngleZ = (float)Math.toRadians(ip.PARENT_ROTATION_ANGLE.getZ());

		parentModel.render(matrixStack, renderBuffer, combinedLight, combinedOverlay, r, g, b, a);
  }

	private InteractiveParameters ip;

  /**
   * InteractiveParameters holds the interactive parameters used to tweak the model in real time.
   * One model instance is active at a given time; when a model is active and the user issues a command:
   * mbedebug param    (eg /mbedebug param parent_delta 3.5)
   * mbedebug param3d  (eg /mbedebug paramvec3d parent_rotation_point 0.0 1.0 0.0)
   * then the parameter is copied into the InteractiveParameters for that model instance
   *
   * Typical usage:
   * 1) When creating a new TileEntity that will render this model:
   *    a) createDefault()
   *    b) makeThisModelActive()
   * 2) If loading the TileEntity from disk:
   *    a) createFromNBT()
   *
   * 3) User types mbedebug param or mbedebug param3d commands to manipulate the settings
   *    a) before creating the model or rendering it, call updateFromDebugSettingsIfActive()
   * 4) To switch to a different model instance, call makeThisModelActive()
   * 5) Save to NBT: putToNBT()
    */
	static class InteractiveParameters {
    public Vector3f PARENT_CORNER = new Vector3f(-8.0F, -8.0F, -8.0F);
    public Vector3f PARENT_ROTATION_POINT = new Vector3f(0.0F,0.0F,0.0F);
    public Vector3f PARENT_ROTATION_ANGLE = new Vector3f(0.0F, 0.0F, 0.0F);
    public net.minecraft.util.math.Vec3i PARENT_TEXTURE_ORIGIN = new net.minecraft.util.math.Vec3i(0, 0, 0);
    public net.minecraft.util.math.Vec3i PARENT_BLOCK_DIMENSIONS = new net.minecraft.util.math.Vec3i(16, 16, 16);
    public Double PARENT_DELTA = 0.0;

    public Vector3f CHILD_CORNER = new Vector3f(-0.5F, -1.5F, -1.0F);
    public Vector3f CHILD_ROTATION_POINT = new Vector3f(0.0F,0.0F,0.0F);
    public Vector3f CHILD_ROTATION_ANGLE = new Vector3f(0.0F, 0.0F, 0.0F);
    public net.minecraft.util.math.Vec3i CHILD_TEXTURE_ORIGIN = new net.minecraft.util.math.Vec3i(0, 0, 0);
    public net.minecraft.util.math.Vec3i CHILD_BLOCK_DIMENSIONS = new net.minecraft.util.math.Vec3i(1, 3, 2);
    public Double CHILD_DELTA = 0.0;

    public Double USE_ENTITY_MODEL_TRANSFORMATIONS = 1D;  // less than 0.5 means don't apply vanilla's entity model transformations (scale, translate by 1.5)

    public Vector3f MODEL_TRANSLATE = new Vector3f(0.5F, 1.0F, 0.5F);

    private InteractiveParameters() {
      vector3fSettings.put("parent_corner", new Vector3fSetting("parent_corner", PARENT_CORNER));
      vector3fSettings.put("parent_rotation_point", new Vector3fSetting("parent_rotation_point", PARENT_ROTATION_POINT));
      vector3fSettings.put("parent_rotation_angle", new Vector3fSetting("parent_rotation_angle", PARENT_ROTATION_ANGLE));
      vec3iSettings.put("parent_texture_origin", new Vec3iSetting("parent_texture_origin", PARENT_TEXTURE_ORIGIN));
      vec3iSettings.put("parent_block_dimensions", new Vec3iSetting("parent_block_dimensions", PARENT_BLOCK_DIMENSIONS));
      doubleSettings.put("parent_delta", new DoubleSetting("parent_delta", PARENT_DELTA));

      vector3fSettings.put("child_corner", new Vector3fSetting("child_corner", CHILD_CORNER));
      vector3fSettings.put("child_rotation_point", new Vector3fSetting("child_rotation_point", CHILD_ROTATION_POINT));
      vector3fSettings.put("child_rotation_angle", new Vector3fSetting("child_rotation_angle", CHILD_ROTATION_ANGLE));
      vec3iSettings.put("child_texture_origin", new Vec3iSetting("child_texture_origin", CHILD_TEXTURE_ORIGIN));
      vec3iSettings.put("child_block_dimensions", new Vec3iSetting("child_block_dimensions", CHILD_BLOCK_DIMENSIONS));
      doubleSettings.put("child_delta", new DoubleSetting("child_delta", CHILD_DELTA));

      vector3fSettings.put("model_translate", new Vector3fSetting("model_translate", MODEL_TRANSLATE));
      doubleSettings.put("use_entity_model_transformations", new DoubleSetting("use_entity_model_transformations", USE_ENTITY_MODEL_TRANSFORMATIONS));
    }

    private void copyMapsToFields() {
      PARENT_CORNER = vector3fSettings.get("parent_corner").value;
      PARENT_ROTATION_POINT = vector3fSettings.get("parent_rotation_point").value;
      PARENT_ROTATION_ANGLE = vector3fSettings.get("parent_rotation_angle").value;
      PARENT_TEXTURE_ORIGIN = vec3iSettings.get("parent_texture_origin").value;
      PARENT_BLOCK_DIMENSIONS = vec3iSettings.get("parent_block_dimensions").value;
      PARENT_DELTA = doubleSettings.get("parent_delta").value;

      CHILD_CORNER = vector3fSettings.get("child_corner").value;
      CHILD_ROTATION_POINT = vector3fSettings.get("child_rotation_point").value;
      CHILD_ROTATION_ANGLE = vector3fSettings.get("child_rotation_angle").value;
      CHILD_TEXTURE_ORIGIN = vec3iSettings.get("child_texture_origin").value;
      CHILD_BLOCK_DIMENSIONS = vec3iSettings.get("child_block_dimensions").value;
      CHILD_DELTA = doubleSettings.get("child_delta").value;

      MODEL_TRANSLATE = vector3fSettings.get("model_translate").value;
      USE_ENTITY_MODEL_TRANSFORMATIONS = doubleSettings.get("use_entity_model_transformations").value;
    }

    public static InteractiveParameters createDefault() {
      return new InteractiveParameters();
    }

    public static InteractiveParameters createFromNBT(CompoundNBT nbt) {
      InteractiveParameters retval = new InteractiveParameters();
      for (Vector3fSetting entry : retval.vector3fSettings.values()) entry.updateFromNBT(nbt);
      for (Vec3iSetting entry : retval.vec3iSettings.values()) entry.updateFromNBT(nbt);
      for (DoubleSetting entry : retval.doubleSettings.values()) entry.updateFromNBT(nbt);
      retval.copyMapsToFields();
      return retval;
    }

    public void makeThisModelActive(Vector3d worldPos) {
      DebugSettings.setDebugParameterVec3d("model_active", worldPos);
    }

    public void makeThisModelActive(BlockPos worldPos) {
      Vector3d vec3dWorldPos = new Vector3d(worldPos.getX(), worldPos.getY(), worldPos.getZ());
      DebugSettings.clearDebugParameterVec3d("model_active");

      for (Vector3fSetting entry : vector3fSettings.values()) entry.clearDebugSetting();
      for (Vec3iSetting entry : vec3iSettings.values()) entry.clearDebugSetting();
      for (DoubleSetting entry : doubleSettings.values()) entry.clearDebugSetting();

      DebugSettings.setDebugParameterVec3d("model_active", vec3dWorldPos);
    }

    public void updateFromDebugSettingsIfActive(Vec3i worldPos) {
      Optional<Vector3d> activeModel = DebugSettings.getDebugParameterVec3d("model_active");
      Vector3d worldPosVec3d = new Vector3d(worldPos.getX(), worldPos.getY(), worldPos.getZ());

      if (!activeModel.isPresent() || worldPosVec3d.distanceTo(activeModel.get()) > 0.1) return;
      for (Vector3fSetting entry : vector3fSettings.values()) entry.updateFromDebugSettings();
      for (Vec3iSetting entry : vec3iSettings.values()) entry.updateFromDebugSettings();
      for (DoubleSetting entry : doubleSettings.values()) entry.updateFromDebugSettings();

      copyMapsToFields();
    }

    public void putToNBT(CompoundNBT nbt) {
      for (Vector3fSetting entry : vector3fSettings.values()) entry.putIntoNBT(nbt);
      for (Vec3iSetting entry : vec3iSettings.values()) entry.putIntoNBT(nbt);
      for (DoubleSetting entry : doubleSettings.values()) entry.putIntoNBT(nbt);
    }

    public void printToConsole() {
      LOGGER.warn(this.toString());
    }

    /**
     * Autogenerated
     * @return
     */
    @Override
    public String toString() {
      return "InteractiveParameters{" +
              "PARENT_CORNER=" + PARENT_CORNER +
              "\nPARENT_ROTATION_POINT=" + PARENT_ROTATION_POINT +
              "\nPARENT_ROTATION_ANGLE=" + PARENT_ROTATION_ANGLE +
              "\nPARENT_TEXTURE_ORIGIN=" + PARENT_TEXTURE_ORIGIN +
              "\nPARENT_BLOCK_DIMENSIONS=" + PARENT_BLOCK_DIMENSIONS +
              "\nPARENT_DELTA=" + PARENT_DELTA +
              "\nCHILD_CORNER=" + CHILD_CORNER +
              "\nCHILD_ROTATION_POINT=" + CHILD_ROTATION_POINT +
              "\nCHILD_ROTATION_ANGLE=" + CHILD_ROTATION_ANGLE +
              "\nCHILD_TEXTURE_ORIGIN=" + CHILD_TEXTURE_ORIGIN +
              "\nCHILD_BLOCK_DIMENSIONS=" + CHILD_BLOCK_DIMENSIONS +
              "\nCHILD_DELTA=" + CHILD_DELTA +
              "\nMODEL_TRANSLATE=" + MODEL_TRANSLATE +
              '}';
    }

    private Map<String, Vector3fSetting> vector3fSettings = new HashMap<>();
    private Map<String, Vec3iSetting> vec3iSettings = new HashMap<>();
    private Map<String, DoubleSetting> doubleSettings = new HashMap<>();

    private static class Vector3fSetting {
      public Vector3fSetting(String name, Vector3f defaultValue) {
        this.name = name;
        this.value = defaultValue;
      }
      
      public Vector3f get() {return value;}
      
      public void updateFromDebugSettings() {
        Optional<Vector3d> debugValue = DebugSettings.getDebugParameterVec3d(name);
        if (debugValue.isPresent()) {
          value = new Vector3f((float) debugValue.get().x, (float) debugValue.get().y, (float) debugValue.get().z);
        }
      }
      
      public void updateFromNBT(CompoundNBT nbt) {
        try {
          CompoundNBT nbtVector3f = nbt.getCompound(name);
          value = new Vector3f(nbtVector3f.getFloat("x"), nbtVector3f.getFloat("y"), nbtVector3f.getFloat("z"));
        } catch (Exception e) {  // ignore all errors
        }        
      }

      private void putIntoNBT(CompoundNBT nbt) {
        CompoundNBT vector3fTag = new CompoundNBT();        // CompoundNBT is similar to a Java HashMap
        vector3fTag.putFloat("x", value.getX());
        vector3fTag.putFloat("y", value.getY());
        vector3fTag.putFloat("z", value.getZ());
        nbt.put(name, vector3fTag);
      }

      public void clearDebugSetting() {DebugSettings.clearDebugParameterVec3d(name);}

      private String name;
      private Vector3f value;
    }

    private static class Vec3iSetting
    {
      public Vec3iSetting(String name, Vec3i defaultValue) {
        this.name = name;
        this.value = defaultValue;
      }

      public Vec3i get() {return value;}

      public void updateFromDebugSettings() {
        Optional<Vector3d> debugValue = DebugSettings.getDebugParameterVec3d(name);
        if (debugValue.isPresent()) {
          value = new Vec3i((int) debugValue.get().x, (int) debugValue.get().y, (int) debugValue.get().z);
        }
      }

      public void updateFromNBT(CompoundNBT nbt) {
        try {
          CompoundNBT nbtVec3i = nbt.getCompound(name);
          value = new Vec3i(nbtVec3i.getInt("x"), nbtVec3i.getInt("y"), nbtVec3i.getInt("z"));
        } catch (Exception e) {  // ignore all errors
        }
      }

      private void putIntoNBT(CompoundNBT nbt) {
        CompoundNBT vec3iTag = new CompoundNBT();        // CompoundNBT is similar to a Java HashMap
        vec3iTag.putFloat("x", value.getX());
        vec3iTag.putFloat("y", value.getY());
        vec3iTag.putFloat("z", value.getZ());
        nbt.put(name, vec3iTag);
      }

      public void clearDebugSetting() {DebugSettings.clearDebugParameterVec3d(name);}

      private String name;
      private Vec3i value;
    }

    private static class DoubleSetting
    {
      public DoubleSetting(String name, Double defaultValue) {
        this.name = name;
        this.value = defaultValue;
      }

      public Double get() {return value;}

      public void updateFromDebugSettings() {
        Optional<Double> debugValue = DebugSettings.getDebugParameter(name);
        if (debugValue.isPresent()) {
          value = debugValue.get();
        }
      }

      public void updateFromNBT(CompoundNBT nbt) {
        try {
          value = nbt.getDouble(name);
        } catch (Exception e) {  // ignore all errors
        }
      }

      public void putIntoNBT(CompoundNBT nbt) {
        nbt.putDouble(name, value);
      }

      public void clearDebugSetting() {DebugSettings.clearDebugParameter(name);}

      private String name;
      private Double value;
    }
  }

  private static final Logger LOGGER = LogManager.getLogger();
}

package minecraftbyexample.testingarea;

import net.minecraft.client.renderer.block.model.ItemTransformVec3f;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.client.model.TRSRTransformation;
import org.apache.commons.lang3.tuple.Pair;

import javax.vecmath.Matrix3f;
import javax.vecmath.Vector3f;

/**
 * Created by EveryoneElse on 7/02/2016.
 */
public class TestTransformation {

  public boolean test1(World worldIn, EntityPlayer playerIn) {
    Vector3f translation = new Vector3f(0, 0, 0);
    Vector3f scale = new Vector3f(1, 1, 1);
    Vector3f rotation = new Vector3f(0, 0, 0);

    ItemTransformVec3f testVec = new ItemTransformVec3f(rotation, translation, scale);
    TRSRTransformation transformation = new TRSRTransformation(testVec);


    Vector3f newScale = transformation.getScale();
    Pair<Matrix3f, Vector3f> affinePair = transformation.toAffine(transformation.getMatrix());
    Matrix3f newRotationScaleM = affinePair.getLeft();
    Vector3f newTranslation = affinePair.getRight();



    System.out.format("translation:%s\n", translation.toString());
    System.out.format("newTranslation:%s\n", newTranslation.toString());
    System.out.format("scale:%s\n", scale.toString());
    System.out.format("newScale:%s\n", newScale.toString());

    Matrix3f newRotationM = (Matrix3f)newRotationScaleM.clone();
    final float EPSILON = 0.001F;
    if (Math.abs(newScale.getX()) > EPSILON) newRotationM.setM00(newRotationM.getM00() / newScale.getX());
    if (Math.abs(newScale.getY()) > EPSILON) newRotationM.setM11(newRotationM.getM11() / newScale.getY());
    if (Math.abs(newScale.getZ()) > EPSILON) newRotationM.setM22(newRotationM.getM22() / newScale.getZ());

    System.out.format("newRotationScaleM:%s\n", newRotationScaleM.toString());
    System.out.format("newRotationM:%s\n", newRotationM.toString());
    return true;

    /*
    The rotation matrix is the product of the three euler angle rotation matrices

    R = Ry.Rx.Rz

    This matrix will have the values:

    m00 = cy.cz + sx.sy.sz
    m01 = -cy.sz + sx.sy.cz
    m02 = cx.sy
    m10 = cx.sz
    m11 = cx.cz
    m12 = -sx
    m20 = -sy.cz + sx.cy.sz
    m21 = sy.sz + sx.cy.cz
    m22 = cx.cy

    where cx = cos(x), sx = sin(x), cy = cos(y), etc

    This can be converted back to the euler angles x, y, z as follows:
    1) x1 = arcsin(-m12) or x2 = pi() - arcsin(-m12)
       We can choose either one of these answers to get a correct answer- there are two sets of euler angles that
       will give identical orientation in space.
       One will have cx = positive, the other will have cx = negative, i.e. cx2 = -cx1

    2) a) if cx == 0 (sx==1) then we have 'gimbal lock' and there is no unique solution for y and z.
            https://sundaram.wordpress.com/2013/03/08/mathematical-reason-behind-gimbal-lock-in-euler-angles/
      In this case,
       m00 = cy.cz + sy.sz = cos(y-z)
       m01 = -cy.sz + sy.cz = sin(y-z)
      Solve using m01 / m00 = sin(y-z)/cos(y-z) = tan(y-z) hence y-z = atan2(m01, m00)
      Then choose arbitrary extremes of z1 = 0 -> y1; and y2 = 0 -> z2

      b) otherwise :
       calculate sy from m02 and cy from m22 -> y = atan2(sy, cy)
       calculate sz from m10 and cz from m11 -> z = atan2(sz, cz)

      Return both solutions.

     */



  }

}

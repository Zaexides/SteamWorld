package zaexides.steamworld.client.rendering.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;

/**
 * Fishagger - Zae
 * Created using Tabula 7.0.0
 */
public class ModelVillangler extends ModelBase {
    public ModelRenderer body;
    public ModelRenderer head;
    public ModelRenderer armLeft;
    public ModelRenderer armRight;
    public ModelRenderer legLeft;
    public ModelRenderer legRight;
    public ModelRenderer illiciumNear;
    public ModelRenderer illiciumFar;
    public ModelRenderer esca;

    public ModelVillangler() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.body = new ModelRenderer(this, 32, 0);
        this.body.setRotationPoint(0.0F, 14.0F, 0.0F);
        this.body.addBox(-4.0F, -5.0F, -2.0F, 8, 10, 4, 0.0F);
        this.head = new ModelRenderer(this, 0, 0);
        this.head.setRotationPoint(0.0F, -5.0F, 0.0F);
        this.head.addBox(-4.0F, -10.0F, -4.0F, 8, 10, 8, 0.0F);
        this.esca = new ModelRenderer(this, 38, 18);
        this.esca.setRotationPoint(0.0F, -7.8F, 0.0F);
        this.esca.addBox(-1.0F, -4.0F, -1.0F, 2, 4, 2, 0.0F);
        this.setRotateAngle(esca, 0.3490658503988659F, 0.0F, 0.0F);
        this.armRight = new ModelRenderer(this, 0, 18);
        this.armRight.setRotationPoint(-4.0F, -5.0F, 0.0F);
        this.armRight.addBox(-2.0F, 0.0F, -1.5F, 2, 9, 3, 0.0F);
        this.setRotateAngle(armRight, 0.3490658503988659F, 0.0F, -0.5235987755982988F);
        this.armLeft = new ModelRenderer(this, 0, 18);
        this.armLeft.mirror = true;
        this.armLeft.setRotationPoint(4.0F, -5.0F, 0.0F);
        this.armLeft.addBox(0.0F, 0.0F, -1.5F, 2, 9, 3, 0.0F);
        this.setRotateAngle(armLeft, 0.3490658503988659F, 0.0F, 0.5235987755982988F);
        this.legLeft = new ModelRenderer(this, 10, 18);
        this.legLeft.mirror = true;
        this.legLeft.setRotationPoint(3.0F, 5.0F, 0.0F);
        this.legLeft.addBox(-2.0F, 0.0F, -2.0F, 4, 5, 4, 0.0F);
        this.illiciumFar = new ModelRenderer(this, 34, 19);
        this.illiciumFar.setRotationPoint(0.0F, -7.6F, -0.6F);
        this.illiciumFar.addBox(-0.5F, -8.0F, -0.5F, 1, 8, 1, 0.0F);
        this.setRotateAngle(illiciumFar, 2.2689280275926285F, 0.0F, 0.0F);
        this.legRight = new ModelRenderer(this, 10, 18);
        this.legRight.setRotationPoint(-3.0F, 5.0F, 0.0F);
        this.legRight.addBox(-2.0F, 0.0F, -2.0F, 4, 5, 4, 0.0F);
        this.illiciumNear = new ModelRenderer(this, 26, 18);
        this.illiciumNear.setRotationPoint(0.0F, -9.5F, -3.0F);
        this.illiciumNear.addBox(-1.0F, -8.0F, -1.0F, 2, 8, 2, 0.0F);
        this.setRotateAngle(illiciumNear, 0.5235987755982988F, 0.0F, 0.0F);
        this.body.addChild(this.head);
        this.illiciumFar.addChild(this.esca);
        this.body.addChild(this.armRight);
        this.body.addChild(this.armLeft);
        this.body.addChild(this.legLeft);
        this.illiciumNear.addChild(this.illiciumFar);
        this.body.addChild(this.legRight);
        this.head.addChild(this.illiciumNear);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) 
    { 
    	if(((EntityAgeable)entity).isChild())
    	{
    		GlStateManager.pushMatrix();
    		GlStateManager.scale(0.5, 0.5, 0.5);
    		GlStateManager.translate(0, 24.0 * f5, 0);
    		this.body.render(f5);
    		GlStateManager.popMatrix();
    	}
    	else
    		this.body.render(f5);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
    
    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
    		float headPitch, float scaleFactor, Entity entityIn) 
    {
    	float swingCos = (float)Math.cos(ageInTicks * 0.5 + Math.PI * 0.5) * 0.16f * limbSwingAmount;
    	this.legRight.rotateAngleX = (float)Math.sin(ageInTicks * -0.5) * 0.4f * limbSwingAmount;
    	this.legRight.offsetY = (float)Math.min(swingCos, 0.0f);
    	this.legLeft.rotateAngleX = -this.legRight.rotateAngleX;
    	this.legLeft.offsetY = (float)Math.min(-swingCos, 0.0f);
    	
    	this.head.rotateAngleY = netHeadYaw * 0.017453292f;
    	this.head.rotateAngleX = headPitch * 0.017453292f;
    	
    	this.illiciumNear.rotateAngleX = (float)Math.cos(limbSwing) * 0.325f * limbSwingAmount;
    	this.illiciumFar.rotateAngleX = illiciumNear.rotateAngleX * 0.7f;
    	this.illiciumFar.rotateAngleY = (float)Math.sin(limbSwing * 0.98) * 0.18f * limbSwingAmount;
    	this.esca.rotateAngleX = 0.3490658503988659F - illiciumNear.rotateAngleX - illiciumFar.rotateAngleX;
    	this.esca.rotateAngleY = -illiciumFar.rotateAngleY;
    	this.illiciumNear.rotateAngleX += 0.5235987755982988F;
    	this.illiciumFar.rotateAngleX += 2.2689280275926285F;
    }
}

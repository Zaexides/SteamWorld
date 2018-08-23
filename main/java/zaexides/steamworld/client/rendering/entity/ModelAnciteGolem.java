package zaexides.steamworld.client.rendering.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import zaexides.steamworld.entity.EntityAnciteGolem;

/**
 * AnciteGolem - Zae
 * Created using Tabula 7.0.0
 */
public class ModelAnciteGolem extends ModelBase {
    public ModelRenderer body;
    public ModelRenderer topBody;
    public ModelRenderer legLeft;
    public ModelRenderer legRight;
    public ModelRenderer armLeft;
    public ModelRenderer armRight;
    public ModelRenderer head;

    public ModelAnciteGolem() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.armLeft = new ModelRenderer(this, 0, 9);
        this.armLeft.setRotationPoint(5.5F, -4.0F, -2.0F);
        this.armLeft.addBox(-1.5F, -1.0F, -1.5F, 3, 10, 3, 0.0F);
        this.head = new ModelRenderer(this, 28, 9);
        this.head.setRotationPoint(0.0F, -5.0F, -2.0F);
        this.head.addBox(-3.0F, -5.0F, -3.0F, 6, 6, 6, 0.0F);
        this.topBody = new ModelRenderer(this, 24, 0);
        this.topBody.setRotationPoint(0.0F, -2.5F, 2.0F);
        this.topBody.addBox(-4.0F, -5.0F, -4.0F, 8, 5, 4, 0.0F);
        this.legLeft = new ModelRenderer(this, 12, 9);
        this.legLeft.setRotationPoint(0, 2.5F, 2.0F);
        this.legLeft.addBox(0, 0.0F, -4.0F, 4, 8, 4, 0.0F);
        this.legRight = new ModelRenderer(this, 12, 9);
        this.legRight.mirror = true;
        this.legRight.setRotationPoint(0.0F, 2.5F, 2.0F);
        this.legRight.addBox(-4.0F, 0.0F, -4.0F, 4, 8, 4, 0.0F);
        this.armRight = new ModelRenderer(this, 0, 9);
        this.armRight.setRotationPoint(-5.5F, -4.0F, -2.0F);
        this.armRight.addBox(-1.5F, -1.0F, -1.5F, 3, 10, 3, 0.0F);
        this.body = new ModelRenderer(this, 0, 0);
        this.body.setRotationPoint(0.0F, 13.5F, 0.0F);
        this.body.addBox(-4.0F, -2.5F, -2.0F, 8, 5, 4, 0.0F);
        this.topBody.addChild(this.armLeft);
        this.topBody.addChild(this.head);
        this.body.addChild(this.topBody);
        this.body.addChild(this.legLeft);
        this.body.addChild(this.legRight);
        this.topBody.addChild(this.armRight);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
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
    	float awakeningStep = ((EntityAnciteGolem)entityIn).getAwakeningStep();
    	setDefaultRotationAndPosition(awakeningStep);
    	
    	this.legRight.rotateAngleX = lerp(this.legRight.rotateAngleX, (float)Math.sin(limbSwing * 0.5) * limbSwingAmount, awakeningStep);
    	this.legLeft.rotateAngleX = lerp(this.legLeft.rotateAngleX, -this.legRight.rotateAngleX, awakeningStep);
    	this.armRight.rotateAngleX = lerp(this.armRight.rotateAngleX, this.legLeft.rotateAngleX * 0.2f, awakeningStep);
    	this.armLeft.rotateAngleX = lerp(this.armLeft.rotateAngleX, this.legRight.rotateAngleX * 0.2f - 1.5f, awakeningStep);
    	
    	this.head.rotateAngleY = lerp(this.head.rotateAngleY, netHeadYaw * 0.01745f, awakeningStep);
    	this.head.rotateAngleX = lerp(this.head.rotateAngleX, headPitch * 0.01745f, awakeningStep);
    }
    
    private void setDefaultRotationAndPosition(float awakeningPercentage)
    {
    	final float HALF_CIRCLE = (float)Math.PI;
    	
    	this.topBody.rotateAngleX = lerp(0, -HALF_CIRCLE, 1-awakeningPercentage);
    	this.armLeft.rotateAngleX = this.armRight.rotateAngleX = lerp(0, HALF_CIRCLE, 1-awakeningPercentage);
    	this.armLeft.rotationPointY = this.armRight.rotationPointY = lerp(-4, -1, 1-awakeningPercentage);
    	this.legRight.rotationPointX = lerp(0, -4, 1-awakeningPercentage);
    	this.legLeft.rotationPointX = lerp(0, 4, 1-awakeningPercentage);
    	this.legRight.rotationPointY = this.legLeft.rotationPointY = lerp(2.5f, -0.5f, 1-awakeningPercentage);
    	this.head.rotationPointY = lerp(-5, 0, 1-awakeningPercentage);
    	this.body.rotationPointY = lerp(13.5f, 16.5f, 1-awakeningPercentage);
    	
    	this.legRight.rotateAngleX = 0;
    	this.legLeft.rotateAngleX = 0;
    	this.head.rotateAngleY = 0;
    	this.head.rotateAngleX = 0;
    }
    
    private float lerp(float a, float b, float t)
    {
    	return a + t * (b - a);
    }
}

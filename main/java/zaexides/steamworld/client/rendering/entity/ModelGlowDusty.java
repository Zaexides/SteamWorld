package zaexides.steamworld.client.rendering.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import zaexides.steamworld.entity.EntityGlowDusty;

/**
 * GlowDusty - Zae
 * Created using Tabula 7.0.0
 */
public class ModelGlowDusty extends ModelBase {
    public ModelRenderer body;
    public ModelRenderer wingLeft;
    public ModelRenderer wingRight;
    
    private static final float INT_TO_COLOR_FLOAT = 1.0f / 255.0f;

    public ModelGlowDusty() {
        this.textureWidth = 32;
        this.textureHeight = 32;
        this.body = new ModelRenderer(this, 0, 0);
        this.body.setRotationPoint(0.0F, 21.0F, 0.0F);
        this.body.addBox(-3.0F, -3.0F, -3.0F, 6, 6, 6, 0.0F);
        this.wingRight = new ModelRenderer(this, 0, 12);
        this.wingRight.mirror = true;
        this.wingRight.setRotationPoint(-3.0F, 0.0F, 0.0F);
        this.wingRight.addBox(-5.0F, 0.0F, -1.5F, 5, 1, 3, 0.0F);
        this.wingLeft = new ModelRenderer(this, 0, 12);
        this.wingLeft.setRotationPoint(3.0F, 0.0F, 0.0F);
        this.wingLeft.addBox(0.0F, 0.0F, -1.5F, 5, 1, 3, 0.0F);
        this.body.addChild(this.wingRight);
        this.body.addChild(this.wingLeft);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) 
    { 
    	GlStateManager.pushMatrix();
    	
    	int color = ((EntityGlowDusty)entity).getColor();
    	GlStateManager.color(((color >> 16) & 255) * INT_TO_COLOR_FLOAT, ((color >> 8) & 255) * INT_TO_COLOR_FLOAT, (color & 255) * INT_TO_COLOR_FLOAT);
    	
    	if(((EntityAgeable)entity).isChild())
    	{
    		GlStateManager.pushMatrix();
    		GlStateManager.scale(0.5, 0.5, 0.5);
    		GlStateManager.translate(0, 24.0 * f5, 0);
    		renderBase(entity, f5);
    		GlStateManager.popMatrix();
    	}
    	else
    		renderBase(entity, f5);
    	
    	GlStateManager.color(1, 1, 1);
        GlStateManager.popMatrix();
    }
    
    private void renderBase(Entity entity, float f5)
    {
    	GlStateManager.pushMatrix();
        GlStateManager.translate(this.body.offsetX, this.body.offsetY, this.body.offsetZ);
        GlStateManager.translate(this.body.rotationPointX * f5, this.body.rotationPointY * f5, this.body.rotationPointZ * f5);
        GlStateManager.scale(0.5D, 0.5D, 0.5D);
        GlStateManager.translate(-this.body.offsetX, -this.body.offsetY, -this.body.offsetZ);
        GlStateManager.translate(-this.body.rotationPointX * f5, -this.body.rotationPointY * f5, -this.body.rotationPointZ * f5);
        this.body.render(f5);
        GlStateManager.popMatrix();
    }
    
    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
    		float headPitch, float scaleFactor, Entity entityIn) 
    {
    	double velY = entityIn.motionY;
    	
    	if(velY >= 0)
    		this.wingLeft.rotateAngleZ = (float)Math.cos(ageInTicks * 10 * (velY + 0.2)) * 1.0f;
    	else
    		this.wingLeft.rotateAngleZ = 0.2f;
    	
    	this.wingRight.rotateAngleZ = -this.wingLeft.rotateAngleZ;
    	
    	this.body.rotateAngleY = netHeadYaw * 0.017453292f;
    	this.body.rotateAngleX = headPitch * 0.017453292f;
    }
}
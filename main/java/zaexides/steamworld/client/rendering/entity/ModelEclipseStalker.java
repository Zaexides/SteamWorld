package zaexides.steamworld.client.rendering.entity;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

/**
 * Eclipse Stalker - Zae
 * Created using Tabula 7.0.0
 */
public class ModelEclipseStalker extends ModelBase {
    public ModelRenderer Body;
    public ModelRenderer Skull;
    public ModelRenderer ArmR;
    public ModelRenderer ArmL;
    public ModelRenderer Head;
    public ModelRenderer BladeR;
    public ModelRenderer BladeL;

    public ModelEclipseStalker() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.Body = new ModelRenderer(this, 0, 16);
        this.Body.setRotationPoint(0.0F, 16.0F, 0.0F);
        this.Body.addBox(-4.0F, -6.0F, -2.0F, 8, 12, 4, 0.0F);
        this.setRotateAngle(Body, 0.12217304763960307F, 0.0F, 0.0F);
        this.BladeR = new ModelRenderer(this, 32, 0);
        this.BladeR.setRotationPoint(-1.0F, 9.0F, 1.0F);
        this.BladeR.addBox(-0.5F, 0.0F, -0.5F, 1, 10, 2, 0.0F);
        this.setRotateAngle(BladeR, 1.5707963267948966F, 0.0F, 0.0F);
        this.ArmR = new ModelRenderer(this, 24, 20);
        this.ArmR.setRotationPoint(-4.0F, -6.0F, 0.0F);
        this.ArmR.addBox(-2.0F, 0.0F, -1.0F, 2, 10, 2, 0.0F);
        this.setRotateAngle(ArmR, -2.0943951023931953F, 0.0F, 0.0F);
        this.ArmL = new ModelRenderer(this, 24, 20);
        this.ArmL.setRotationPoint(4.0F, -6.0F, 0.0F);
        this.ArmL.addBox(0.0F, 0.0F, -1.0F, 2, 10, 2, 0.0F);
        this.setRotateAngle(ArmL, -2.0943951023931953F, 0.0F, 0.0F);
        this.Head = new ModelRenderer(this, 0, 0);
        this.Head.setRotationPoint(0.0F, 10.0F, 0.0F);
        this.Head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F);
        this.BladeL = new ModelRenderer(this, 32, 0);
        this.BladeL.setRotationPoint(1.0F, 9.0F, 1.0F);
        this.BladeL.addBox(-0.5F, 0.0F, -0.5F, 1, 10, 2, 0.0F);
        this.setRotateAngle(BladeL, 1.5707963267948966F, 0.0F, 0.0F);
        this.Skull = new ModelRenderer(this, 32, 4);
        this.Skull.setRotationPoint(0.0F, 10.0F, 0.0F);
        this.Skull.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, -0.5F);
        this.ArmR.addChild(this.BladeR);
        this.Body.addChild(this.ArmR);
        this.Body.addChild(this.ArmL);
        this.ArmL.addChild(this.BladeL);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 0.9F);
        this.Body.render(f5);
        this.Head.render(f5);
        GlStateManager.disableBlend();
        this.Skull.render(f5);
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
    	float armLRot = (float) Math.cos(ageInTicks * 0.041f) * 0.1f;
    	float armRRot = (float) Math.sin(ageInTicks * 0.041f + 0.67f) * 0.1f;
    	
    	this.ArmL.rotateAngleX = -2.1f + armLRot;
    	this.ArmR.rotateAngleX = -2.1f + armRRot;
    	
    	this.Head.rotateAngleY = this.Skull.rotateAngleY = netHeadYaw * 0.017453292f;
    	this.Head.rotateAngleX = this.Skull.rotateAngleX = headPitch * 0.017453292f;
    }
}

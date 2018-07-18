package zaexides.steamworld.client.rendering.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * SkyFish - Zae
 * Created using Tabula 7.0.0
 */
public class ModelSkyFish extends ModelBase {
    public ModelRenderer Body;
    public ModelRenderer FinCon;
    public ModelRenderer Fin;
    public ModelRenderer Head;
    public ModelRenderer WingL;
    public ModelRenderer WingR;
    
    private static final float CHILD_SCALE = 0.5f;

    public ModelSkyFish() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.Head = new ModelRenderer(this, 0, 0);
        this.Head.setRotationPoint(0.0F, 0.0F, -5.0F);
        this.Head.addBox(-2.0F, -3.0F, -5.0F, 4, 6, 5, 0.0F);
        this.WingR = new ModelRenderer(this, 12, 5);
        this.WingR.mirror = true;
        this.WingR.setRotationPoint(-2.5F, 0.0F, 0.0F);
        this.WingR.addBox(-20.0F, 0.0F, -2.5F, 20, 1, 6, 0.0F);
        this.Body = new ModelRenderer(this, 0, 14);
        this.Body.setRotationPoint(0.0F, 20.0F, 0.0F);
        this.Body.addBox(-2.5F, -4.0F, -5.0F, 5, 8, 10, 0.0F);
        this.WingL = new ModelRenderer(this, 12, 5);
        this.WingL.setRotationPoint(2.5F, 0.0F, 0.0F);
        this.WingL.addBox(0.0F, 0.0F, -2.5F, 20, 1, 6, 0.0F);
        this.Fin = new ModelRenderer(this, 50, 17);
        this.Fin.setRotationPoint(0.0F, 0.0F, 6.0F);
        this.Fin.addBox(-1.0F, -5.0F, 0.0F, 2, 10, 5, 0.0F);
        this.FinCon = new ModelRenderer(this, 30, 20);
        this.FinCon.setRotationPoint(0.0F, 0.0F, 5.0F);
        this.FinCon.addBox(-2.0F, -3.0F, 0.0F, 4, 6, 6, 0.0F);
        this.Body.addChild(this.Head);
        this.Body.addChild(this.WingR);
        this.Body.addChild(this.WingL);
        this.FinCon.addChild(this.Fin);
        this.Body.addChild(this.FinCon);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        
    	float scale = isChild ? CHILD_SCALE : 1.0f;
    	this.Body.render(f5 * scale);
    }
    
    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
    		float headPitch, float scaleFactor, Entity entityIn) 
    {
    	float finRotation = (float)Math.cos(limbSwing * 0.5f) * 1.5f * limbSwingAmount;
    	this.FinCon.rotateAngleY = finRotation;
    	this.Fin.rotateAngleY = finRotation;
    	
    	float wingRotation = 0.01f;
    	if(!entityIn.onGround)
    		wingRotation = (float)Math.sin(ageInTicks * 1.5f - limbSwing * 0.5f) * 0.3f;
    	this.WingL.rotateAngleZ = wingRotation;
    	this.WingR.rotateAngleZ = -wingRotation;
    	
    	this.Head.rotateAngleY = netHeadYaw * 0.017453292f;
    	this.Head.rotateAngleX = headPitch * 0.017453292f;
    }
}

package zaexides.steamworld.client.rendering.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * PropellorShell - Zae
 * Created using Tabula 7.0.0
 */
public class ModelPropellorShell extends ModelBase {
    public ModelRenderer Body;
    public ModelRenderer LowerBody;
    public ModelRenderer UpperBody;
    public ModelRenderer Propellor;
    public ModelRenderer Blade1;
    public ModelRenderer Blade2;
    public ModelRenderer Blade3;

    public ModelPropellorShell() {
        this.textureWidth = 64;
        this.textureHeight = 16;
        this.Blade1 = new ModelRenderer(this, 0, 10);
        this.Blade1.setRotationPoint(0.0F, -0.5F, 0.0F);
        this.Blade1.addBox(0.0F, 0.0F, -1.0F, 10, 1, 2, 0.0F);
        this.setRotateAngle(Blade1, 0.0F, -1.0471975511965976F, 0.0F);
        this.Propellor = new ModelRenderer(this, 0, 0);
        this.Propellor.setRotationPoint(0.0F, -3.0F, 0.0F);
        this.Propellor.addBox(-0.5F, -1.0F, -0.5F, 1, 1, 1, 0.0F);
        this.UpperBody = new ModelRenderer(this, 52, 0);
        this.UpperBody.setRotationPoint(0.0F, -2.0F, 0.0F);
        this.UpperBody.addBox(-1.5F, -3.0F, -1.5F, 3, 3, 3, 0.0F);
        this.Body = new ModelRenderer(this, 0, 0);
        this.Body.setRotationPoint(0.0F, 20.0F, 0.0F);
        this.Body.addBox(-3.0F, -2.0F, -3.0F, 6, 4, 6, 0.0F);
        this.Blade2 = new ModelRenderer(this, 0, 10);
        this.Blade2.setRotationPoint(0.0F, -0.5F, 0.0F);
        this.Blade2.addBox(0.0F, 0.0F, -1.0F, 10, 1, 2, 0.0F);
        this.setRotateAngle(Blade2, 0.0F, 1.0471975511965976F, 0.0F);
        this.LowerBody = new ModelRenderer(this, 24, 0);
        this.LowerBody.setRotationPoint(0.0F, 2.0F, 0.1F);
        this.LowerBody.addBox(-3.5F, 0.0F, -3.5F, 7, 2, 7, 0.0F);
        this.Blade3 = new ModelRenderer(this, 0, 10);
        this.Blade3.setRotationPoint(0.0F, -0.5F, 0.0F);
        this.Blade3.addBox(0.0F, 0.0F, -1.0F, 10, 1, 2, 0.0F);
        this.setRotateAngle(Blade3, 0.0F, 3.141592653589793F, 0.0F);
        this.Propellor.addChild(this.Blade1);
        this.UpperBody.addChild(this.Propellor);
        this.Body.addChild(this.UpperBody);
        this.Propellor.addChild(this.Blade2);
        this.Body.addChild(this.LowerBody);
        this.Propellor.addChild(this.Blade3);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.Body.render(f5);
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
    	this.Propellor.rotateAngleY = (float) (ageInTicks * 0.5f + entityIn.motionY * 2.0f);
    }
}

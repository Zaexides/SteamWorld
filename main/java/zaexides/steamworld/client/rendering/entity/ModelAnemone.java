package zaexides.steamworld.client.rendering.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * Anemone - Zae
 * Created using Tabula 7.0.0
 */
public class ModelAnemone extends ModelBase {
    public ModelRenderer Body;
    public ModelRenderer Head;
    public ModelRenderer Fangs1;
    public ModelRenderer Fangs2;
    public ModelRenderer Fangs3;
    public ModelRenderer Fangs4;

    public ModelAnemone() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.Fangs2 = new ModelRenderer(this, 0, 0);
        this.Fangs2.setRotationPoint(0.0F, -5.0F, 5.5F);
        this.Fangs2.addBox(-6.0F, -3.0F, 0.0F, 12, 3, 1, 0.0F);
        this.Fangs4 = new ModelRenderer(this, 0, 0);
        this.Fangs4.setRotationPoint(-6.5F, -5.0F, 0.0F);
        this.Fangs4.addBox(-6.0F, -3.0F, 0.0F, 12, 3, 1, 0.0F);
        this.setRotateAngle(Fangs4, 0.0F, 1.5707963267948966F, 0.0F);
        this.Body = new ModelRenderer(this, 0, 6);
        this.Body.setRotationPoint(0.0F, 19.0F, 0.0F);
        this.Body.addBox(-8.0F, -5.0F, -8.0F, 16, 10, 16, 0.0F);
        this.Fangs3 = new ModelRenderer(this, 0, 0);
        this.Fangs3.setRotationPoint(5.5F, -5.0F, 0.0F);
        this.Fangs3.addBox(-6.0F, -3.0F, 0.0F, 12, 3, 1, 0.0F);
        this.setRotateAngle(Fangs3, 0.0F, 1.5707963267948966F, 0.0F);
        this.Fangs1 = new ModelRenderer(this, 0, 0);
        this.Fangs1.setRotationPoint(0.0F, -5.0F, -6.5F);
        this.Fangs1.addBox(-6.0F, -3.0F, 0.0F, 12, 3, 1, 0.0F);
        this.Head = new ModelRenderer(this, 3, 7);
        this.Head.setRotationPoint(0.0F, -5.0F, 0.0F);
        this.Head.addBox(-7.0F, -5.0F, -7.0F, 14, 5, 14, 0.0F);
        this.Head.addChild(this.Fangs2);
        this.Head.addChild(this.Fangs4);
        this.Head.addChild(this.Fangs3);
        this.Head.addChild(this.Fangs1);
        this.Body.addChild(this.Head);
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
    	this.Fangs1.rotateAngleX = (float)Math.sin(ageInTicks * 0.04) * 0.3f + 0.1f;
    	this.Fangs2.rotateAngleX = (float)Math.sin(ageInTicks * 0.04) * -0.3f - 0.1f;
    	this.Fangs3.rotateAngleZ = (float)Math.sin(ageInTicks * 0.04) * 0.3f + 0.1f;
    	this.Fangs4.rotateAngleZ = (float)Math.sin(ageInTicks * 0.04) * -0.3f - 0.1f;
    }
}

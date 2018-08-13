package zaexides.steamworld.client.rendering.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.util.math.Vec3d;
import zaexides.steamworld.entity.EntityGlowDusty;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

import org.lwjgl.opengl.GL11;

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
        renderAura(entity, f5);
        GlStateManager.popMatrix();
    }
    
    private void renderAura(Entity entity, float f5)
    {
    	//TODO get this to billboard
    	GlStateManager.pushMatrix();
    	GlStateManager.translate(0, 2, 0);
    	GlStateManager.scale(32, 32, 32);
    	GlStateManager.rotate(1.0f, 0, 0, 0);
    	
    	float rotationX = ActiveRenderInfo.getRotationX();
    	float rotationXY = ActiveRenderInfo.getRotationXY();
    	float rotationXZ = ActiveRenderInfo.getRotationXZ();
    	float rotationYZ = ActiveRenderInfo.getRotationYZ();
    	float rotationZ = ActiveRenderInfo.getRotationZ();
    	Vec3d[] avec3d = new Vec3d[] {new Vec3d((double)(-rotationX * f5 - rotationXY * f5), (double)(-rotationZ * f5), (double)(-rotationYZ * f5 - rotationXZ * f5)), new Vec3d((double)(-rotationX * f5 + rotationXY * f5), (double)(rotationZ * f5), (double)(-rotationYZ * f5 + rotationXZ * f5)), new Vec3d((double)(rotationX * f5 + rotationXY * f5), (double)(rotationZ * f5), (double)(rotationYZ * f5 + rotationXZ * f5)), new Vec3d((double)(rotationX * f5 - rotationXY * f5), (double)(-rotationZ * f5), (double)(rotationYZ * f5 - rotationXZ * f5))};
    	
    	Tessellator tessellator = Tessellator.getInstance();
    	BufferBuilder builder = tessellator.getBuffer();
    	
    	builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
    	
    	builder.pos(avec3d[0].x, avec3d[0].y, avec3d[0].z).tex(0.0f, 0.5f).endVertex();
    	builder.pos(avec3d[1].x, avec3d[1].y, avec3d[1].z).tex(0.5f, 0.5f).endVertex();
    	builder.pos(avec3d[2].x, avec3d[2].y, avec3d[2].z).tex(0.5f, 1.0f).endVertex();
    	builder.pos(avec3d[3].x, avec3d[3].y, avec3d[3].z).tex(0.0f, 1.0f).endVertex();
    	
    	tessellator.draw();
    	
    	GlStateManager.popMatrix();
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}

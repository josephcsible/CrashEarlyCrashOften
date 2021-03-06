/*
CrashEarlyCrashOften Minecraft Mod
Copyright (C) 2018  Joseph C. Sible

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
*/

package josephcsible.crashearlycrashoften;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import static org.objectweb.asm.Opcodes.*;

import net.minecraft.launchwrapper.IClassTransformer;

public class CecoClassTransformer implements IClassTransformer {
	private static String worldName, loadedEntityListField, nonNullListName, nonNullListCreateName;

	public static void setObfuscated(boolean isObfuscated) {
		if(isObfuscated) {
			worldName = "amu";
			loadedEntityListField = "e";
			nonNullListName = "fi";
			nonNullListCreateName = "a";
		} else {
			worldName = "net/minecraft/world/World";
			loadedEntityListField = "loadedEntityList";
			nonNullListName = "net/minecraft/util/NonNullList";
			nonNullListCreateName = "create";
		}
	}

	private static void transformWorldConstructor(MethodNode method) {
		/*
		 * We're trying to change this:
		 * this.loadedEntityList = Lists.<Entity>newArrayList();
		 * INVOKESTATIC com/google/common/collect/Lists.newArrayList()Ljava/util/ArrayList; (our target instruction)
		 * PUTFIELD net/minecraft/world/World.loadedEntityList : Ljava/util/List; (what we look for to find our target instruction)
		 * to this:
		 * this.loadedEntityList = NonNullList.<Entity>create();
		 * INVOKESTATIC net/minecraft/util/NonNullList.create()Lnet/minecraft/util/NonNullList; (what we're changing)
		 * PUTFIELD net/minecraft/world/World.loadedEntityList : Ljava/util/List;
		 */
		for(AbstractInsnNode instruction : method.instructions.toArray()) {
			if(instruction.getOpcode() != PUTFIELD) continue;
			FieldInsnNode fieldInstruction = (FieldInsnNode)instruction;
			if(worldName.equals(fieldInstruction.owner) && loadedEntityListField.equals(fieldInstruction.name) && "Ljava/util/List;".equals(fieldInstruction.desc)) {
				MethodInsnNode targetNode = (MethodInsnNode)instruction.getPrevious();
				targetNode.owner = nonNullListName;
				targetNode.name = nonNullListCreateName;
				targetNode.desc = "()L" + nonNullListName + ";";
				return;
			}
		}
		throw new RuntimeException("Failed to find the part of the method to patch!");
	}

	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		if(!("net.minecraft.world.World".equals(transformedName))) return basicClass;
		ClassNode cn = new ClassNode();
		ClassReader cr = new ClassReader(basicClass);
		cr.accept(cn, ClassReader.SKIP_FRAMES);
		for(MethodNode method : cn.methods) {
			if("<init>".equals(method.name)) {
				transformWorldConstructor(method);
				ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
				cn.accept(cw);
				System.out.println("Successfully patched World.loadedEntityList to be a NonNullList");
				return cw.toByteArray();
			}
		}
		throw new RuntimeException("Failed to find the method to patch!");
	}

}

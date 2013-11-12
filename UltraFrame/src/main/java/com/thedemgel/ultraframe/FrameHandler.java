
package com.thedemgel.ultraframe;

import java.util.ArrayList;
import java.util.List;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.ItemFrame;


public class FrameHandler {
	private static List<NPC> frameNPCs;
	private static List<ItemFrame> frames;

	static {
		frameNPCs = new ArrayList<>();
		frames = new ArrayList<>();
	}

	public static boolean isFrameOwned(ItemFrame frame) {
		return frames.contains(frame);
	}

	public static void registerFrames() {

	}

	public static void registerNPC(NPC npc) {
		if (!frameNPCs.contains(npc)) {
			frameNPCs.add(npc);
		}
	}

	public static void unregisterFrames() {

	}

	public static void unregisterNPC(NPC npc) {
		if(frameNPCs.contains(npc)) {
			frameNPCs.remove(npc);
		}
	}
}

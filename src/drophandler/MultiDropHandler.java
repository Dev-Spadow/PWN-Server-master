package drophandler;

import com.arlania.model.Position;
import com.arlania.world.entity.impl.npc.NPC;
import com.arlania.world.entity.impl.player.Player;

public interface MultiDropHandler {

	
	public abstract void handleDrop(Player player, NPC npc, Position position);
	
	
	public abstract void clearDamageMap();
}

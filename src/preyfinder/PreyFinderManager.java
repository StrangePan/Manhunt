package preyfinder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;


public class PreyFinderManager implements IPreyFinderManager {

	HashMap<String, PreyFinder> finders = new HashMap<String, PreyFinder>();

	public void registerNewFinder(Player p)
	{
		removeFinder(p.getName());
		finders.put(p.getName(), new PreyFinder(p));
	}

	public List<PreyFinder> getExpiredFinders()
	{
		List<PreyFinder> expired = new ArrayList<PreyFinder>();
		
		for (PreyFinder finder : finders.values())
		{
			if (finder.isExpired())
			{
				expired.add(finder);
			}
		}
		
		return expired;
	}

	public boolean removeFinder(PreyFinder f)
	{
		if (finders.containsValue(f.player))
		{
			return removeFinder(f.player);
		}
		else
		{
			return false;
		}
	}

	public boolean removeFinder(String s)
	{
		if (finders.containsKey(s))
		{
			finders.remove(s);
			return true;
		}
		else
		{
			return false;
		}
	}

}

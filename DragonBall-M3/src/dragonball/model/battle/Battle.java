package dragonball.model.battle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import dragonball.model.attack.Attack;
import dragonball.model.attack.MaximumCharge;
import dragonball.model.attack.PhysicalAttack;
import dragonball.model.attack.SuperAttack;
import dragonball.model.attack.UltimateAttack;
import dragonball.model.cell.Collectible;
import dragonball.model.character.fighter.Fighter;
import dragonball.model.character.fighter.PlayableFighter;
import dragonball.model.character.fighter.Saiyan;
import dragonball.model.exceptions.NotEnoughKiException;
import dragonball.model.exceptions.NotEnoughSenzuBeansException;
import dragonball.model.player.Player;

public class Battle implements Serializable
{
	private BattleOpponent me;
	private BattleOpponent foe;
	private BattleOpponent attacker;
	private boolean meBlocking;
	private boolean foeBlocking;
	private BattleListener listener;

	public Battle(BattleOpponent me, BattleOpponent foe)
	{
		this.me = me;
		this.foe = foe;
		this.attacker = me;

		// set current values appropriately
		Fighter meFighter = (Fighter) me;
		meFighter.setHealthPoints(meFighter.getMaxHealthPoints());
		meFighter.setKi(0);
		meFighter.setStamina(meFighter.getMaxStamina());
		// reset a saiyan's transformation state in case it was transformed in a previous battle
		if (me instanceof Saiyan)
		{
			Saiyan meSaiyan = (Saiyan) me;
			meSaiyan.setTransformed(false);
		}

		Fighter foeFighter = (Fighter) foe;
		foeFighter.setHealthPoints(foeFighter.getMaxHealthPoints());
		foeFighter.setKi(0);
		foeFighter.setStamina(foeFighter.getMaxStamina());
	}

	public BattleOpponent getMe()
	{
		return me;
	}

	public BattleOpponent getFoe()
	{
		return foe;
	}

	public BattleOpponent getAttacker()
	{
		return attacker;
	}

	public BattleOpponent getDefender()
	{
		return attacker == me ? foe : me;
	}

	public boolean isMeBlocking()
	{
		return meBlocking;
	}

	public boolean isFoeBlocking()
	{
		return foeBlocking;
	}

	public ArrayList<Attack> getAssignedAttacks()
	{
		Fighter attackerFighter = (Fighter) attacker;

		ArrayList<Attack> attacks = new ArrayList<>();
		// make sure to include the physical attack as well
		attacks.add(new PhysicalAttack());
		attacks.addAll(attackerFighter.getSuperAttacks());
		attacks.addAll(attackerFighter.getUltimateAttacks());
		return attacks;
	}

	public void switchTurn()
	{
		attacker = getDefender();
	}

	public void endTurn()
	{
		// reset block mode
		if (attacker == me && foeBlocking)
		{
			foeBlocking = false;
		}
		else if (attacker == foe && meBlocking)
		{
			meBlocking = false;
		}

		// if i'm dead
		if (((Fighter) me).getHealthPoints() == 0)
		{
			// tell everyone my opponent won
			notifyOnBattleEvent(new BattleEvent(this, BattleEventType.ENDED, foe));
			// if my opponent is dead
		}
		else if (((Fighter) foe).getHealthPoints() == 0)
		{
			// tell everyone i won
			notifyOnBattleEvent(new BattleEvent(this, BattleEventType.ENDED, me));
		}else{
			switchTurn();

			getAttacker().onDefenderTurn();
			getDefender().onAttackerTurn();

			notifyOnBattleEvent(new BattleEvent(this, BattleEventType.NEW_TURN));
		}
	}

	public void start()
	{
		notifyOnBattleEvent(new BattleEvent(this, BattleEventType.STARTED));
		notifyOnBattleEvent(new BattleEvent(this, BattleEventType.NEW_TURN));
	}

	// used to automate turn for opponent a.k.a. ai
	public Attack play() throws NotEnoughKiException
	{
		int maxDamage = 0;
		SuperAttack myMaxSA = null;
		UltimateAttack myMaxUA = null;
		boolean MC = false;
		
		ArrayList<SuperAttack> sAttacks= ((Fighter)foe).getSuperAttacks();
		for(SuperAttack attack : sAttacks)
		{
			if(attack.getDamage() > maxDamage)
			{
				maxDamage = attack.getDamage();
				myMaxSA = attack;
			}
			if(attack instanceof MaximumCharge)
			{
				MC = true;
			}
		}
		
		maxDamage = 0;
		
		ArrayList<UltimateAttack> uAttacks= ((Fighter)foe).getUltimateAttacks();
		for(UltimateAttack attack : uAttacks)
		{
			if(attack.getDamage() > maxDamage)
			{
				maxDamage = attack.getDamage();
				myMaxUA = attack;
			}
		}
		
		maxDamage = 0;
		SuperAttack yourMaxSA = null;
		UltimateAttack yourMaxUA = null;
		
		sAttacks= ((Fighter)me).getSuperAttacks();
		for(SuperAttack attack : sAttacks)
		{
			if(attack.getDamage() > maxDamage)
			{
				maxDamage = attack.getDamage();
				yourMaxSA = attack;
			}
		}
		
		maxDamage = 0;
		
		uAttacks= ((Fighter)me).getUltimateAttacks();
		for(UltimateAttack attack : uAttacks)
		{
			if(attack.getDamage() > maxDamage)
			{
				maxDamage = attack.getDamage();
				yourMaxUA = attack;
			}
		}
		
		int yourHealth = ((Fighter)me).getHealthPoints();
		//int yourMaxHealth = ((Fighter)me).getMaxHealthPoints();
		int yourStamina = ((Fighter)me).getStamina()*100;
		int yourKi = ((Fighter)me).getKi();
		int yourPD = ((Fighter)me).getPhysicalDamage() + 50;
		int yourBD = ((Fighter)me).getBlastDamage();
		
		int myHealth = ((Fighter)foe).getHealthPoints();
		int myMaxHealth = ((Fighter)foe).getMaxHealthPoints();
		int myStamina = ((Fighter)foe).getStamina()*100;
		int myKi = ((Fighter)foe).getKi(); 
		int myPD = ((Fighter)foe).getPhysicalDamage() + 50;
		int myBD = ((Fighter)foe).getBlastDamage();
		
		Attack attack = null;
		
		if(meBlocking)
		{
			if (yourMaxUA != null)
			{
				if(yourKi >=2 && yourMaxUA.getDamage() + yourBD > myHealth)
				{
					if(yourMaxUA.getDamage() + yourBD > myHealth + myStamina)
					{
						attack = opponentAttack(myMaxSA, myMaxUA, myPD, myBD, myKi, yourHealth, yourStamina, MC);
					} else {
						block();
					}
				} else if(yourKi >=2 && yourMaxUA.getDamage() + yourBD > myMaxHealth/4) {
					block();
				} else {
					if (myMaxUA != null)
					{
						if ((myMaxUA.getDamage() + myBD > yourStamina && myKi >= 3) || (myMaxSA.getDamage() + myBD > yourStamina && myKi > 0) || myPD > yourStamina)
						{
							attack = opponentAttack(myMaxSA, myMaxUA, myPD, myBD, myKi, yourHealth, yourStamina, MC);
						} else {
							block();
						}
					} else {
						if ((myMaxSA.getDamage() + myBD > yourStamina && myKi > 0) || myPD > yourStamina)
						{
							attack = opponentAttack(myMaxSA, myMaxUA, myPD, myBD, myKi, yourHealth, yourStamina, MC);
						} else {
							block();
						}
					}
				}
			} else if(yourMaxSA != null) {
				if(yourMaxSA.getDamage() + yourBD > myHealth)
				{
					if(yourMaxSA.getDamage() + yourBD > myHealth + myStamina)
					{
						attack = opponentAttack(myMaxSA, myMaxUA, myPD, myBD, myKi, yourHealth, yourStamina, MC);
					} else {
						block();
					}
				} else if(yourMaxSA.getDamage() + yourBD > myMaxHealth/4) {
					block();
				} else {
					if (myMaxUA != null)
					{
						if ((myMaxUA.getDamage() + myBD > yourStamina && myKi >= 3) || (myMaxSA.getDamage() + myBD > yourStamina && myKi > 0) || myPD > yourStamina)
						{
							attack = opponentAttack(myMaxSA, myMaxUA, myPD, myBD, myKi, yourHealth, yourStamina, MC);
						} else {
							block();
						}
					} else {
						if ((myMaxSA.getDamage() + myBD > yourStamina && myKi > 0) || myPD > yourStamina)
						{
							attack = opponentAttack(myMaxSA, myMaxUA, myPD, myBD, myKi, yourHealth, yourStamina, MC);
						} else {
							block();
						}
					}
				}
			} else {
				if(yourPD > myHealth)
				{
					if(yourPD > myHealth + myStamina)
					{
						attack = opponentAttack(myMaxSA, myMaxUA, myPD, myBD, myKi, yourHealth, yourStamina, MC);
					} else {
						block();
					}
				} else if(yourPD > myMaxHealth/4) {
					block();
				} else {
					if (myMaxUA != null)
					{
						if ((myMaxUA.getDamage() + myBD > yourStamina && myKi >= 3) || (myMaxSA.getDamage() + myBD > yourStamina && myKi > 0) || myPD > yourStamina)
						{
							attack = opponentAttack(myMaxSA, myMaxUA, myPD, myBD, myKi, yourHealth, yourStamina, MC);
						} else {
							block();
						}
					} else {
						if ((myMaxSA.getDamage() + myBD > yourStamina && myKi > 0) || myPD > yourStamina)
						{
							attack = opponentAttack(myMaxSA, myMaxUA, myPD, myBD, myKi, yourHealth, yourStamina, MC);
						} else {
							block();
						}
					}
				}
			}
		}else{
			if (yourMaxUA != null)
			{
				if(yourKi >=2 && yourMaxUA.getDamage() + yourBD > myHealth)
				{
					if(yourMaxUA.getDamage() + yourBD > myHealth + myStamina)
					{
						attack = opponentAttack(myMaxSA, myMaxUA, myPD, myBD, myKi, yourHealth, yourStamina, MC);
					} else {
						block();
					}
				} else if(yourKi >=2 && yourMaxUA.getDamage() + yourBD > myMaxHealth/4) {
					block();
				} else {
					attack = opponentAttack(myMaxSA, myMaxUA, myPD, myBD, myKi, yourHealth, yourStamina, MC);
				}
			} else if(yourMaxSA != null) {
				if(yourMaxSA.getDamage() + yourBD > myHealth)
				{
					if(yourMaxSA.getDamage() + yourBD > myHealth + myStamina)
					{
						attack = opponentAttack(myMaxSA, myMaxUA, myPD, myBD, myKi, yourHealth, yourStamina, MC);
					} else {
						block();
					}
				} else if(yourMaxSA.getDamage() + yourBD > myMaxHealth/4) {
					block();
				} else {
					attack = opponentAttack(myMaxSA, myMaxUA, myPD, myBD, myKi, yourHealth, yourStamina, MC);
				}
			} else {
				if(yourPD > myHealth)
				{
					if(yourPD > myHealth + myStamina)
					{
						attack = opponentAttack(myMaxSA, myMaxUA, myPD, myBD, myKi, yourHealth, yourStamina, MC);
					} else {
						block();
					}
				} else if(yourPD > myMaxHealth/4) {
					block();
				} else {
					attack = opponentAttack(myMaxSA, myMaxUA, myPD, myBD, myKi, yourHealth, yourStamina, MC);
				}
			}
		}
		return attack;
//		if (new Random().nextInt(100) > 15)
//		{
//			ArrayList<Attack> attacks = getAssignedAttacks();
//			Attack randomAttack;
//			randomAttack = attacks.get(new Random().nextInt(attacks.size()));
//			attack(randomAttack);
//		}else{
//			block();
//		}
	}
	
	public Attack opponentAttack (SuperAttack myMaxSA, UltimateAttack myMaxUA, int myPD, int myBD, int myKi, int yourHealth, int yourStamina, 
			boolean MC) throws NotEnoughKiException
	{
		if(meBlocking)
		{
			if(myPD > yourHealth + yourStamina)
			{
				attack(new PhysicalAttack());
				return new PhysicalAttack();
			}else if(myKi == 0){
				if(MC)
				{
					attack(new MaximumCharge());
					return new MaximumCharge();
				}else{
					attack(new PhysicalAttack());
					return new PhysicalAttack();
				}
			}else if(myKi < 3){
				if (myKi != 2)
				{
					if (myMaxSA.getDamage() + myBD > yourHealth + yourStamina) {
						attack(myMaxSA);
						return myMaxSA;
					} else if (MC) {
						attack(new MaximumCharge());
						return new MaximumCharge();
					} else if (myPD > (yourHealth + yourStamina) / 8) {
						attack(new PhysicalAttack());
						return new PhysicalAttack();
					} else {
						attack(myMaxSA);
						return myMaxSA;
					}
				}else if (myMaxUA != null){
					int healthAfter = yourHealth + yourStamina - myPD;
					if(myMaxUA.getDamage() + myBD > healthAfter)
					{
						attack(new PhysicalAttack());
						return new PhysicalAttack();
					}else{
						if (myMaxSA.getDamage() + myBD > yourHealth + yourStamina) {
							attack(myMaxSA);
							return myMaxSA;
						} else if (MC) {
							attack(new MaximumCharge());
							return new MaximumCharge();
						} else if (myPD > (yourHealth + yourStamina) / 8) {
							attack(new PhysicalAttack());
							return new PhysicalAttack();
						} else {
							attack(myMaxSA);
							return myMaxSA;
						}
					}
				}else{
					if (myPD > yourStamina || myMaxSA.getDamage() + myBD > yourStamina) {
						if (myPD > myMaxSA.getDamage() + myBD)
						{
							attack(new PhysicalAttack());
							return new PhysicalAttack();
						}else{
							attack(myMaxSA);
							return myMaxSA;
						}
					}
				}
			}else if (myKi >= 3){
				if (myMaxUA != null) {
					attack(myMaxUA);
					return myMaxUA;
				}else{
					attack(myMaxSA);
					return myMaxSA;
				}
			}
		}else{
			if(myPD > yourHealth)
			{
				attack(new PhysicalAttack());
				return new PhysicalAttack();
			}else if(myKi == 0){
				if(MC)
				{
					attack(new MaximumCharge());
					return new MaximumCharge();
				}else{
					attack(new PhysicalAttack());
					return new PhysicalAttack();
				}
			}else if(myKi < 3){
				if (myKi != 2)
				{
					if (myMaxSA.getDamage() + myBD > yourHealth) {
						attack(myMaxSA);
						return myMaxSA;
					} else if (MC) {
						attack(new MaximumCharge());
						return new MaximumCharge();
					} else if (myPD > yourHealth / 8) {
						attack(new PhysicalAttack());
						return new PhysicalAttack();
					} else {
						attack(myMaxSA);
						return myMaxSA;
					}
				}else if (myMaxUA != null){
					int healthAfter = yourHealth - myPD;
					if(myMaxUA.getDamage() + myBD > healthAfter)
					{
						attack(new PhysicalAttack());
						return new PhysicalAttack();
					}else{
						if (myMaxSA.getDamage() + myBD > yourHealth){
							attack(myMaxSA);
							return myMaxSA;
						} else if (MC) {
							attack(new MaximumCharge());
							return new MaximumCharge();
						} else if (myPD > yourHealth / 8) {
							attack(new PhysicalAttack());
							return new PhysicalAttack();
						} else {
							attack(myMaxSA);
							return myMaxSA;
						}
					}
				}else{
					if (myPD > myMaxSA.getDamage() + myBD)
					{
						attack(new PhysicalAttack());
						return new PhysicalAttack();
					}else{
						attack(myMaxSA);
						return myMaxSA;
					}
				}
			}else if (myKi >= 3){
				if (myMaxUA != null) {
					attack(myMaxUA);
					return myMaxUA;
				}else{
					attack(myMaxSA);
					return myMaxSA;
				}
			}
		}
		return null;
	}

	// perform an attack and end turn
	public void attack(Attack attack) throws NotEnoughKiException
	{
		attack.onUse(attacker, getDefender(),
				(attacker == me && foeBlocking) || (attacker == foe && meBlocking));

		notifyOnBattleEvent(new BattleEvent(this, BattleEventType.ATTACK, attack));

		endTurn();
	}

	// perform a block and end turn
	public void block()
	{
		if (attacker == me)
		{
			meBlocking = true;
		}
		else if (attacker == foe)
		{
			foeBlocking = true;
		}

		notifyOnBattleEvent(new BattleEvent(this, BattleEventType.BLOCK));

		endTurn();
	}

	// use a collectible and end turn
	public void use(Player player, Collectible collectible) throws NotEnoughSenzuBeansException
	{
		switch (collectible)
		{
		case SENZU_BEAN:
			if (player.getSenzuBeans() > 0)
			{
				PlayableFighter activeFighter = player.getActiveFighter();
				activeFighter.setHealthPoints(activeFighter.getMaxHealthPoints());
				activeFighter.setStamina(activeFighter.getMaxStamina());

				player.setSenzuBeans(player.getSenzuBeans() - 1);

				notifyOnBattleEvent(new BattleEvent(this, BattleEventType.USE, collectible));
			}else{
				throw new NotEnoughSenzuBeansException();
			}
			break;
		default:
			break;
		}

		endTurn();
	}

	public void setListener(BattleListener listener)
	{
		this.listener = listener;
	}

	public void notifyOnBattleEvent(BattleEvent e)
	{
		if (listener != null)
		{
			listener.onBattleEvent(e);
		}
	}
}
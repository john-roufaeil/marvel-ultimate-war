# Marvel - Ultimate War
---



Two-player game with the theme of Marvel Ultimate War in which each player chooses 3 champions
and promotes one of the champions to be a leader. The goal of the game is to defeat all champions
of the opponent team. During each turn, a champion has a limited number of action points with which he can move on the board, attack, and cast abilities.
On a leader champion's turn, he can also cast a leader ability, which can only be used once throughout the game. Abilities have three types: Damaging
Abilities, Healing Abilities, and Crowd Control Abilities. Damaging abilities decrease the targets' health points, while healing abilities increase the
targets' health points, whereas crowd control abilities apply a certain effect on targets. There are 10 effects in the game: 5 positive effects (buff)
and 5 negative effects (debuff), each with different usages.

## Contents 
1. OOP concepts (Inheritance - Polymorphism - Abstraction - Encapsulation ).
2. Exception Handling.
3. GUI

## Project Structure
<details>
    
```bash
src/
├── application
│   ├── View.java
│   └── ..............
├── engine
│   ├── Game.java
│   ├── Player.java
│   ├── PriorityQueue.java
│   └── ..............
├── exceptions
│   ├── AbilityUseException.java
│   ├── ChampionDisarmedException.java
│   ├── GameActionException.java
│   ├── InvalidTargetException.java
│   ├── LeaderAbilityAlreadyUsedException.java
│   ├── LeaderNotCurrentException.java
│   ├── NotEnoughResourcesException.java
│   ├── UnallowedMovementException.java
│   └── .......................
└── model
    ├── abilities
    │   ├── Ability.java
    │   ├── AreaOfEffect.java
    │   ├── CrowdControlAbility.java
    │   ├── DamagingAbility.java
    │   └── HealingAbility.java
    ├── effects
    │   ├── Disarm.java
    │   ├── Dodge.java
    │   ├── Effect.java
    │   ├── EffectType.java
    │   ├── Embrace.java
    │   ├── PowerUp.java
    │   ├── Root.java
    │   ├── Shield.java
    │   ├── Shock.java
    │   ├── Silence.java
    │   ├── SpeedUp.java
    │   ├── Stun.java
    │   └── .....................
    └── world
        ├── AntiHero.java
        ├── Champion.java
        ├── Condition.java
        ├── Cover.java
        ├── Damageable.java
        ├── Direction.java
        ├── Hero.java
        ├── Villain.java
        └── ..............

```
</details>



### Contributors:
- [Amir Tarek](https://github.com/amir-awad)
- [Ahmed Monsef](https://github.com/ahmedmonsef184) 
- [John Fayez](https://github.com/John-louis1)

<p align="center">
    <img src="https://user-images.githubusercontent.com/97978852/175838329-ce16ca66-ad66-4292-9576-109c09fb2629.png" width="400" height="200">
</p>
<p align="center">
    <img alt="GitHub" src="https://img.shields.io/github/license/john-louis1/marvel-ultimate-war">
    <img alt="GitHub commits" src="https://badgen.net/github/commits/john-louis1/marvel-ultimate-war/main">
</p>

## Description
This is a two-player game with the theme of Marvel Ultimate War in which each player chooses 3 champions
and promotes one of the champions to be a leader. The goal of the game is to defeat all champions
of the opponent team. During each turn, a champion has a limited number of action points with which he can move on the board, attack, and cast abilities.
On a leader champion's turn, he can also cast a leader ability, which can only be used once throughout the game. Abilities have three types: Damaging
Abilities, Healing Abilities, and Crowd Control Abilities. Damaging abilities decrease the targets' health points, while healing abilities increase the
targets' health points, whereas crowd control abilities apply a certain effect on targets. There are 10 effects in the game: 5 positive effects (buff)
and 5 negative effects (debuff), each with different usages.

## How to Run
It's pretty easy to run the game but a little long, please hold on!
#### Users Installation
1. Install JDK from [here](https://www.oracle.com/java/technologies/downloads/) and JavaFX from [here](https://gluonhq.com/products/javafx/) (make sure to remember where you installed it)
2. Download the game's ZIP file OR fork and clone into your local respository
3. Open the command line terminal (or Windows PowerShell), navigate to the local repository and run: 
```sh
java --module-path "{PATH TO LIB INSTALLED IN STEP 2}" --add-modules javafx.controls,javafx.fxml,javafx.media -jar Ultimate-War.jar
``` 

#### Developers Installation
1. Install JDK from [here](https://www.oracle.com/java/technologies/downloads/), Eclipse from [here](https://www.eclipse.org/downloads/packages/installer), and JavaFX from [here](https://gluonhq.com/products/javafx/) (make sure to remember where you installed it)
2. Download the game's ZIP file OR fork and clone into your local respository
3. Open Eclipse, Navigate to window -> preferences -> search for "user libraries" -> new -> type "JavaFX" -> Add External JARs... -> select the JARs you installed from step 2.
4. Open the project using Eclipse, right click on it and choose Build Path -> Configure Build Path -> Add Library -> User Library -> JavaFX
5. Right Click on the project, Run as -> Run configurations -> Select "View" from left bar -> Arguments and paste the following under VM arguments:
```sh
"--module-path {PATH TO LIB INSTALLED IN STEP 2} --add-modules javafx.controls,javafx.fxml,javafx.media"
``` 
and run.

---

| <img src="https://user-images.githubusercontent.com/97978852/176070833-10bbfb19-1f2f-406a-8dc3-c606320b9065.png"> |
|:--:| 
| **Choosing Champions View** |

<br>

| ![grid](https://user-images.githubusercontent.com/97978852/176071934-7dfcc034-00c1-47c0-b16b-90f69238c32b.png) |
| :--: |
| **Gameplay View** |

<br>

| ![damage](https://user-images.githubusercontent.com/97978852/176072015-17f2b63f-ed61-4088-bdaa-fecda9f3329d.png) |
| :--: |
| **Using Damaging Effect** |

<br>

## Project Structure
<details>
    
```bash
src/
├── application
│   ├── View.java
│   └── ..............
├── engine
│   ├── Game.java
│   ├── Player.java
│   ├── PriorityQueue.java
│   └── ..............
├── exceptions
│   ├── AbilityUseException.java
│   ├── ChampionDisarmedException.java
│   ├── GameActionException.java
│   ├── InvalidTargetException.java
│   ├── LeaderAbilityAlreadyUsedException.java
│   ├── LeaderNotCurrentException.java
│   ├── NotEnoughResourcesException.java
│   ├── UnallowedMovementException.java
│   └── .......................
└── model
    ├── abilities
    │   ├── Ability.java
    │   ├── AreaOfEffect.java
    │   ├── CrowdControlAbility.java
    │   ├── DamagingAbility.java
    │   └── HealingAbility.java
    ├── effects
    │   ├── Disarm.java
    │   ├── Dodge.java
    │   ├── Effect.java
    │   ├── EffectType.java
    │   ├── Embrace.java
    │   ├── PowerUp.java
    │   ├── Root.java
    │   ├── Shield.java
    │   ├── Shock.java
    │   ├── Silence.java
    │   ├── SpeedUp.java
    │   ├── Stun.java
    │   └── .....................
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

package net.sourceforge.kolmafia;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import net.sourceforge.kolmafia.AscensionPath.Path;
import net.sourceforge.kolmafia.KoLConstants.Stat;
import net.sourceforge.kolmafia.objectpool.ItemPool;
import net.sourceforge.kolmafia.preferences.Preferences;

public enum AscensionClass {
  ASTRAL_SPIRIT("Astral Spirit", -1),
  SEAL_CLUBBER("Seal Clubber", 1, "club", 0, "Club Foot"),
  TURTLE_TAMER("Turtle Tamer", 2, "turtle", 0, "Shell Up"),
  PASTAMANCER("Pastamancer", 3, "pasta", 1, "Entangling Noodles"),
  SAUCEROR("Sauceror", 4, "sauce", 1, "Soul Bubble"),
  DISCO_BANDIT("Disco Bandit", 5, "disco", 2),
  ACCORDION_THIEF("Accordion Thief", 6, "accordion", 2, "Accordion Bash"),
  AVATAR_OF_BORIS("Avatar of Boris", 11, "trusty", 0, Path.AVATAR_OF_BORIS, "Broadside"),
  ZOMBIE_MASTER("Zombie Master", 12, "tombstone", 0, Path.ZOMBIE_SLAYER, "Corpse Pile"),
  AVATAR_OF_JARLSBERG(
      "Avatar of Jarlsberg", 14, "path12icon", 1, Path.AVATAR_OF_JARLSBERG, "Blend"),
  AVATAR_OF_SNEAKY_PETE(
      "Avatar of Sneaky Pete", 15, "bigglasses", 2, Path.AVATAR_OF_SNEAKY_PETE, "Snap Fingers"),
  ED("Ed the Undying", 17, "thoth", 1, Path.ACTUALLY_ED_THE_UNDYING, "Curse of Indecision"),
  COWPUNCHER("Cow Puncher", 18, "darkcow", 0, Path.AVATAR_OF_WEST_OF_LOATHING),
  BEANSLINGER("Beanslinger", 19, "beancan", 1, Path.AVATAR_OF_WEST_OF_LOATHING),
  SNAKE_OILER("Snake Oiler", 20, "tinysnake", 2, Path.AVATAR_OF_WEST_OF_LOATHING),
  GELATINOUS_NOOB("Gelatinous Noob", 23, "gelatinousicon", 2, Path.GELATINOUS_NOOB),
  VAMPYRE("Vampyre", 24, "vampirefangs", 1, Path.DARK_GYFFTE, "Chill of the Tomb"),
  PLUMBER("Plumber", 25, "mario_hammer2", -1, Path.PATH_OF_THE_PLUMBER, "Spin Jump"),
  GREY_GOO("Grey Goo", 27, "greygooring", -1, Path.GREY_YOU);

  public static final List<AscensionClass> standardClasses =
      Arrays.asList(
          AscensionClass.SEAL_CLUBBER,
          AscensionClass.TURTLE_TAMER,
          AscensionClass.PASTAMANCER,
          AscensionClass.SAUCEROR,
          AscensionClass.DISCO_BANDIT,
          AscensionClass.ACCORDION_THIEF);

  private final String name;
  private final int id;
  private final String image;
  private final int primeStatIndex;
  private final String stun;

  private final Path path;

  public static Set<AscensionClass> allClasses() {
    return Arrays.stream(values()).filter(a -> a.getId() > -1).collect(Collectors.toSet());
  }

  public static AscensionClass findByPlural(final String plural) {
    if (plural == null || plural.isEmpty()) return null;
    String lowerCasePlural = plural.toLowerCase();
    return Arrays.stream(values())
        .filter(a -> a.getPlural().toLowerCase().contains(lowerCasePlural))
        .findFirst()
        .orElse(null);
  }

  public static AscensionClass find(final String name) {
    if (name == null || name.equals("")) return null;

    String lowerCaseName = name.toLowerCase();

    return Arrays.stream(values())
        .filter(a -> a.getName().toLowerCase().contains(lowerCaseName))
        .findFirst()
        .orElse(null);
  }

  public static AscensionClass find(int id) {
    return Arrays.stream(values()).filter(a -> a.getId() == id).findAny().orElse(null);
  }

  AscensionClass(String name, int id, String image, int primeStatIndex, Path path, String stun) {
    this.name = name;
    this.id = id;
    this.image = image;
    this.primeStatIndex = primeStatIndex;
    this.stun = stun;
    this.path = path;
  }

  AscensionClass(String name, int id, String image, int primeStatIndex, Path path) {
    this(name, id, image, primeStatIndex, path, null);
  }

  AscensionClass(String name, int id, String image, int primeStatIndex, String stun) {
    this(name, id, image, primeStatIndex, Path.NONE, stun);
  }

  AscensionClass(String name, int id, String image, int primeStatIndex) {
    this(name, id, image, primeStatIndex, Path.NONE, null);
  }

  AscensionClass(String name, int id) {
    this(name, id, null, -1);
  }

  public final String getName() {
    return this.name;
  }

  public final int getId() {
    return this.id;
  }

  public final String getPlural() {
    switch (this) {
      case ACCORDION_THIEF:
        return "Accordion Thieves";
      case ED:
        return "Eds the Undying";
      default:
        if (getName().startsWith("Avatar of ")) return "Avatars of " + getName().substring(6);
        return getName() + "s";
    }
  }

  public final String getImage() {
    return this.image;
  }

  public String getStun() {
    if (this.stun != null) {
      return this.stun;
    }

    return Preferences.getBoolean("considerShadowNoodles") ? "Shadow Noodles" : "none";
  }

  public final int getStarterWeapon() {
    return switch (this) {
      case SEAL_CLUBBER -> ItemPool.SEAL_CLUB;
      case TURTLE_TAMER -> ItemPool.TURTLE_TOTEM;
      case PASTAMANCER -> ItemPool.PASTA_SPOON;
      case SAUCEROR -> ItemPool.SAUCEPAN;
      case DISCO_BANDIT -> ItemPool.DISCO_BALL;
      case ACCORDION_THIEF -> ItemPool.STOLEN_ACCORDION;
      default -> -1;
    };
  }

  public final int getPrimeStatIndex() {
    switch (this) {
      case PLUMBER:
      case GREY_GOO:
        long mus = KoLCharacter.getTotalMuscle();
        long mys = KoLCharacter.getTotalMysticality();
        long mox = KoLCharacter.getTotalMoxie();
        return (mus >= mys) ? (mus >= mox ? 0 : 2) : (mys >= mox) ? 1 : 2;
      default:
        return this.primeStatIndex;
    }
  }

  public final Stat getMainStat() {
    return switch (getPrimeStatIndex()) {
      case 0 -> Stat.MUSCLE;
      case 1 -> Stat.MYSTICALITY;
      case 2 -> Stat.MOXIE;
      default -> Stat.NONE;
    };
  }

  public final boolean isStandard() {
    return standardClasses.contains(this);
  }

  public final int getSkillBase() {
    return this.getId() * 1000;
  }

  public final String getInitials() {
    return Arrays.stream(this.name().split("_"))
        .map(word -> String.valueOf(word.charAt(0)))
        .collect(Collectors.joining());
  }

  public Path getPath() {
    return path;
  }

  @Override
  public final String toString() {
    return this.getName();
  }
}

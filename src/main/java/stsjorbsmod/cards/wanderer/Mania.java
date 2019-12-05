package stsjorbsmod.cards.wanderer;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.util.UniqueCardUtils;

import java.util.Iterator;

import static stsjorbsmod.JorbsMod.makeCardPath;

public class Mania extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(Mania.class);

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_CARD_COLOR;

    private static final int COST = 1;
    private static final int BASE_DAMAGE = 0;
    private static final int UPGRADE_PLUS_BASE_DMG = 3;
    private static final int DAMAGE_PER_UNIQUE_CARD = 1;
    private static final int ALL_UNIQUE_ENERGY = 1;
    private static final int ALL_UNIQUE_DRAW = 1;

    public Mania() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        damage = baseDamage = BASE_DAMAGE;
        magicNumber = baseMagicNumber = DAMAGE_PER_UNIQUE_CARD;
        metaMagicNumber = baseMetaMagicNumber = ALL_UNIQUE_ENERGY;
        urMagicNumber = baseUrMagicNumber = ALL_UNIQUE_DRAW;
    }

    @Override
    public int calculateBonusBaseDamage() {
        return UniqueCardUtils.countUniqueCards(AbstractDungeon.player.hand) * magicNumber;
    }

    private boolean isEligibleForExtraEffect() {
        return UniqueCardUtils.countUniqueCards(AbstractDungeon.player.hand) == AbstractDungeon.player.hand.size();
    }

    @Override
    public void applyPowers() {
        int count = calculateBonusBaseDamage();
        if(count>0) {
            super.applyPowers();
            if(this.upgraded) {
                this.rawDescription = cardStrings.UPGRADE_DESCRIPTION + cardStrings.EXTENDED_DESCRIPTION[0];
            }
            else
                this.rawDescription = cardStrings.DESCRIPTION + cardStrings.EXTENDED_DESCRIPTION[0];
            initializeDescription();
        }
    }

    @Override
    public void onMoveToDiscardImpl() {
        if(this.upgraded) {
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
        }
        else
            this.rawDescription = cardStrings.DESCRIPTION;
        initializeDescription();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo);
        if(this.upgraded) {
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
        }
        else
            this.rawDescription = cardStrings.DESCRIPTION;
        this.rawDescription += cardStrings.EXTENDED_DESCRIPTION[0];
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage), AttackEffect.SLASH_VERTICAL));

        if (isEligibleForExtraEffect()) {
            addToBot(new GainEnergyAction(metaMagicNumber));
            addToBot(new DrawCardAction(p, urMagicNumber));
        }
    }

    @Override
    public boolean shouldGlowGold() {
        return isEligibleForExtraEffect();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_BASE_DMG);
            upgradeDescription();
        }
    }
}

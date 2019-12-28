package stsjorbsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

public class ExposeAction extends AbstractGameAction {
    private DamageInfo info;
    private int hpLoss;
    private float baseDuration;


    public ExposeAction(AbstractCreature target, DamageInfo info, int hpLoss) {
        this.info = info;
        this.setValues(target, info);
        this.actionType = ActionType.DAMAGE;
        this.duration = baseDuration = Settings.ACTION_DUR_FAST;
        this.hpLoss = hpLoss;
    }

    @Override
    public void update() {
        if (duration == baseDuration) {
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, AttackEffect.BLUNT_HEAVY));
        }
        tickDuration();
        if (isDone) {
            this.target.damage(this.info);
            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            } else {
                AbstractPlayer p = AbstractDungeon.player;
                addToBot(new LoseHPAction(p, p, hpLoss));
            }
        }
    }
}

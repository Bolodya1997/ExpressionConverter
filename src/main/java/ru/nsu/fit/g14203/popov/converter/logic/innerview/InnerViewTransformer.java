package ru.nsu.fit.g14203.popov.converter.logic.innerview;

import ru.nsu.fit.g14203.popov.converter.optimisation.Transformation;
import ru.nsu.fit.g14203.popov.converter.optimisation.Transformer;

import java.util.ArrayList;
import java.util.List;

public final class InnerViewTransformer extends Transformer {

    private final static List<Transformation> INNER_VIEW_TRANSFORMATIONS = new ArrayList<>();
    static {
        INNER_VIEW_TRANSFORMATIONS.add(new ChainTransformation());
        INNER_VIEW_TRANSFORMATIONS.add(new NotTransformation());
        INNER_VIEW_TRANSFORMATIONS.add(new BracketTransformation());
    }

    public InnerViewTransformer() {
        super(INNER_VIEW_TRANSFORMATIONS);
    }
}

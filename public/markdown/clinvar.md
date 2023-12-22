# ClinVar Getting Started Guide

## What is this data?
ClinVar is an archive of submitted knowledge regarding the clinical significance of genetic variants. A ClinVar SCV is a single assertion about the association of a variant to a disease made by a submitter, typically a laboratory.

Leveraging the GA4GH GKS specifications, we have produced a normalized version of the ClinVar SCV dataset. While there are concepts in ClinVar beyond this dataset (VCV, RCV, etc,), we believe that the SCVs are the foundation of all data provided by ClinVar, as these represent the actual classifications by the submitting laboratories.

## Basic Terminology

**Proposition** an hypothesis regarding the association (e.g. causality) of a subject (e.g. variant) to an object (e.g. disease). It is a 'possible fact'.
Agent - an individual, organization or process.

**Contribution** an activity performed by an agent that contributes to a statement.

**Statement** a descriptive position taken by an agent related to a specific proposition, typically based on evidence and using some method or criteria.

**Method** a set of criteria, a standard operating procedure (e.g. ACMG ISV guidelines 2015), or any type of reasoning.

## Introductory Example

Below is a screenshot of a ClinVar SCV followed by a textual representation of its meaning for use in demonstrating how the key elements map to the message being generated.

TODO Import screenshot

### Textual representation...
On November 15, 2018, the ClinGen RASopathy Variant Curation Expert Panel approved (contribution) the statement that,
NM_004985.5(KRAS):c.458A>T causes Noonan Syndrome (proposition) 
is supported according to the ClinGen RASopathy ACMG Specifications v1 with a classification of Pathogenic. (statement).
### Structured mapping...
```
{
type: VariationGermlinePathogenicityStatement,
contributions: [
{
type: Contribution,
contributor: { type: Agent, ...ClinGen RASopathy Variant Curation Expert Panel },
date: 2018-11-15,
activity: { ...Approver  }
}],
direction: supports,
target_proposition:
{
type: VariationGermlinePathogenicityProposition,
subject:  ... NM_004985.5(KRAS):c.458A>T,
predicate: causes_mendelian_condition,
object:  { type: Disease, ...Noonan Syndrome }
},
classification: {  ... Pathogenic },
method: { ... ClinGen RASopathy ACMG Specifications v1 }
}
```
### Explanation of the concept and mappings

The above example's partial representation of a Variation Germline Pathogenicity Statement illustrates the most salient components to get started. 

The Proposition is intended to represent a scientific hypothesis in the abstract. Every Statement is made relative to a Proposition, in particular whether the application of a specific method, given a set of evidence (optionally specified), supports, refutes, or is uncertain relative to the given Proposition. Each Contribution represents an Activity of one or more Agents, in crafting, reviewing, approving, or any number of specified roles relative to a Statement, at a point in time.

#### The Proposition

A Proposition is defined by a subject and an object, with the relationship between the two defined by a predicate. In this example: NM_004985.5(KRAS):c.458A>T (subject)  causes_mendelian_condition (predicate)  Noonan Syndrome (object). This proposition has a type: VariationGermlinePathogenicityProposition to give a rough sense of what the proposition is about. The type also defines the range of possible predicates, in this case a VariationGermlinePathogenicityProposition can have the predicate ‘causes_mendelian_condition’, ‘increases_risk_for_condition’, or ‘decreases_risk_for_condition’. In the above example, we used shorthand to fill in for structured data, for example NM_004985.5(KRAS):c.458A>T in place of a full CanonicalVariationDescriptor, and Noonan Syndrome in place of a condition object. We will get into detail about how these work later.

#### The Statement

While the Proposition represents an abstract, timeless idea, with no assertion made as to whether or not it’s actually true, the Statement is an affirmation by one or more individuals or groups that the truth of a Proposition is supported, refuted, or inconclusive based on the method used to evaluate available evidence at a point in time. Because the statement is (usually) bound to a method, terms that are specific to that method can be used here, in this case the classification of Pathogenic for the concept described by the Proposition. In this way, the same Proposition can be used as the subject of different Statements applying different Methods (i.e. ACMG 2015, or AMP/ASCO/CAP 2017), representing that they are really about the same thing.

#### The Contribution

Without any sort of attribution, a Statement would be of dubious value. Each Statement has associated with it a set of Contributions, which describe the individuals and organizations that contributed to the Statement, and their role in putting it together (who wrote it, approved it, funded the effort to put it together, and so on).

## Canonical Variation: the proposition subject

This section provides some basic background on how the variation concept, which is the proposition subject in our statement, is represented in the message. In the previous section we show the target_proposition.subject as having the value NM_004985.5(KRAS):c.458A>T, this is an oversimplification. In order to properly represent this variation we need to understand some background about ClinVar and VRS principles

### ClinVar's representation of variation

ClinVar, like other variation-centric databases (i.e. CIViC, ClinGen Allele Registry), uses a policy to ingest and normalize a variety of forms of submitted variations into a consistent and reliable form. When building the VariationGermlinePathogenicityProposition for a ClinVar SCV, it is essential to provide an accurate representation of ClinVar's variation, which we refer to as a Canonical Variation.

**Transformation Principle #1** When transforming data from external sources we should always aim to accurately represent that source's data unless we clearly annotate that it is an altered representation in order to avoid misrepresentation of the original source.

The variation is represented with a VariationDescriptor, which refers to a CanonicalVariation object. In so doing, we turned 

NM_004985.5(KRAS):c.458A>T

Into 
```json
{
  "id": "ga4gh:CLV.zhwrecT5VzM_39t0AjgcJmQi8uJIWpiC",
  "type": "CanonicalVariation",
  "canonical_context":
  {
    "id": "ga4gh:VA.MUEN1WQ1KB49PmHfRG5Ug2j82DsUOqN1",
    "type": "Allele",
    "location":
    {
      "id": "ga4gh:SL.5ZkEW7VoBfQP8Pfkd1P0yIoQHAUyrIXT",
      "type": "SequenceLocation",
      "sequence_id": "ga4gh:SQ.6wlJpONE3oNb4D69ULmEXhqyDZ4vwNfl",
      "start":
      {
        "type": "Number",
        "value": 25209903
      },
      "end":
      {
        "type": "Number",
        "value": 25209904
      }
    },
    "state":
    {
      "type": "LiteralSequenceExpression",
      "sequence": "A"
    }
  }
}
```

The intent is to turn the human readable form of the variation into a computable form. In so doing, we made some changes:

We dropped the gene name, and any extra ‘descriptive’ context. (Some of this might be noted in the descriptor, which we will describe in more detail later)
We also dropped the reference allele. This is redundant information; it can be looked up based on the underlying sequence.

Instead of using the transcript sequence, we describe the sequence on the genomic reference sequence for GRCh38. We did this because ClinVar typically normalizes its variation on GRCh38 genomic reference sequences [REF: ClinVar methods]; our representation reflects ClinVar’s method.

The coordinates are described with inter-residue numbering (0-based interval numbering), rather than 1-based residue numbering.

The subject allele is wrapped in a CanonicalVariation object. This doesn’t add any extra information, but it does change the meaning a little bit:
When we refer to a canonical variation (type: CanonicalVariation), we intend our statement to apply to any variation that can be lifted over and/or projected to/from the canonical_context variation.

This may feel a bit obvious to some, but there is an important and distinct difference in saying our statement only applies to a single specific allele on a specific sequence (transcript, protein or genomic), which we will often refer to as a "contextual allele" or "contextual variation".

## Examples

* Drug Response
* Copy Number Variant
* sample files from gk-pilot?

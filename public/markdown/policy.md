# ClinGen's ClinVar Release Project
This is where we will share all the decisions regarding the transformation of ClinVar's XML data to our format.
This will provide users with complete transparency to the quality and integrity of the data we provide.

A key principle to our transformation process is to preserve both the comprehensiveness and completeness of the clinvar releases (aka snapshots).

## Value Proposition
* ClinVar data is used by many pipelines and systems to provide annotation .... Some pipelines may want to filter or more readily control the records they receive from ClinVar.
* ClinVar updates their data releases on a weekly timeline as global submissions arrive. new data, udpates and removals from individual submitter's impact the aggregated results at a variant (VCV) and variant-disease (RCV) level ... 
* These submissions (SCVs) are the source of all derived or aggregated data that ClinVar generates based on their methodologies... Therefore we focus on delivering the SCV level data in a form that is easy to consume and compute on as needed by downstream systems.
* ClinVar offers their complete set of variation records and associated submissions in two XML formats. Originally in an RCV-centric form dating back to the beginning of ClinVar and more recent Variant-centric (VCV) form which stabilized around July 2019. 
* ClinVar also offers a VCF version of their aggregated VCV data that does not contain the submission level information nor does it contain every variation in ClinVar due to the inability to map some variation types to VCF accurately. 
* By providing a standard json-based format of the ClinVar submission data and associated clinvar variations we aim to enable greater consumption and utility of the data behind ClinVar's aggregated VCV and RCV records.
* The standardized format aims to provide greater access and consistent representation of the full details behind every submission made available in ClinVar's underlying repository.
* The requirement of representing all ClinVar variation using the emerging GKS VRS standard will provide the opportunity to map/associate ClinVar variation to other publicly or privately available datasets that also adopt the GKS VRS standard. NOTE: ClinGen and other GA4GH driver projects are working to promote and enable the use of VRS standards in variant-centric knowledge and data that is used by the genomics community.

# GA4GH GKS dependency
NOTE: this next paragraph is an intro to the dependency we have on GKS - this is a summary but we think we need an additional section to provide a little more depth before pointing the reader to GKS documentation
(Why GKS?) We are committed to formatting these releases on the GA4GH GKS specifications (see ...). As an early adopter of these specifications we are working to improve and expand the standardization of the GKS specification using the ClinVar data as a driver project. (High Level GKS mapping) All clinvar variation is represented using the GKS VRS and VRSATILE (descriptors). The clinvar clinical assertions (aka scvs or submissions) are represented by the VA implementation snapshots for Variation Germline Pathogenicity Statements and additional GK-Pilot extensions an profiles (needed for clinvar scvs that do not fit the VP structure).

(good time to mention the GK-Pilot here)

# Releases
We aim to provide a mirror release of ClinVar's releases as they become available. This will allow users to compare the content to the ClinVar XML to verify the completeness and integrity of our version of the released data. 

We process the ClinVar Variation Archive XML releases as they become available by ClinVar, typically on a weekly basis. These source files are publicly available complete snapshots of the ClinVar data at the point in time they are made available in the ClinVar UI and public repository. ClinVar's policy is to retain the weekly releases for the current month and then only retain the first release of each month indefinitely. The weekly files can be found at http://..... and the monthly files along with any older archived files can be found at http://.

We use the Variation-based ClinVar XML files which became stable in or around July 2019 and we began processing these releases on ??? June 2020. As a result, our historical data that mirrors ClinVar's releases at a monthly frequency for the releases starting with July 2019 through June 2020 and then at a weekly frequency thereafter.

TODO: TN/LB - decision to only deal with full set files. we aim to provide delta files as well (we will need this internally for our streaming work in Clingen in any case).

# Content Decisions (getting started)
We always will provide all records of a given type that are chosen to be included in our version of releases. However, we may choose to provide less than complete representation of all data elements for a given record type with the intent to fill out the remaining elements in subsequent versions. 

## What's in
(reasoning behind what was chosen to be in for v1)
### Variation 
TODO

### Clinical Asssertion (SCVs)
TODO

## What's out
We intentionally do not transform the clinvar generated aggregate data at the variant level (VCV) or variant-condition level (RCV). This is able to be reproduced from the root level SCV data.

### Variation Archive (VCVs)
### RCV Accession (RCVs)
### Submitter
### Gene and Gene Association
### Condition (Diseases and Phenotypes)



# What's in a release?
(variation and scv files)

# dealing with clinvar variation?

## descriptors, canonical and contextual
(we started this in getting started, some reorg may be needed to put the main bits here and reference from the getting started.)

### variation Descriptor 
#### standard mappings vs extensions
-- every HGVS is vrsified and added as a member

### canonical variation
(mappings and rules for transformation)
-- root canonical is always b38 else b37 else b36, else use whatever context is created.

### contextual variation
(mappings and rules for transformation)
-- use SPDI when available
-- use hgvs b38, b37, b36
-- text is based on clinvar variation id
-- relative vs absolute CNVs
-- what if it fails

# scv statement profiles
(reference GK-pilot here for schemas and other GA4GH interoperability notes)

## Pathogenicity
    (germline vs somatic)
    
    Lots todo here (Larry)
    
    
## Drug Response


## Other


# 

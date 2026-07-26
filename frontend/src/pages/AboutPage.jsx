export default function AboutPage() {
    return (
        <div className="page" style={{maxWidth: 720}}>
            <div className="eyebrow">About the Portal</div>
            <h1>Preserving the Written Past</h1>
            <p style={{color: 'var(--parchment)', opacity: 0.85, lineHeight: 1.7}}>
                RareManuscripts is a digital preservation portal built for archivists, conservators,
                and researchers. The catalog holds descriptive records for physical manuscripts held
                in the collection; each entry tracks its conservation condition and its progress
                through our digitization pipeline.
            </p>
            <p style={{color: 'var(--parchment)', opacity: 0.85, lineHeight: 1.7}}>
                Researchers may request study access to restricted works, and once approved, reserve
                a supervised reading-room session. Curators review requests, maintain the catalog,
                and hand fragile works to our digitization service for scanning and restoration.
            </p>
            <p style={{color: 'var(--parchment)', opacity: 0.85, lineHeight: 1.7}}>
                This is a purely informational page and does not change any data — everything else
                in the portal responds to what you do.
            </p>
        </div>
    );
}

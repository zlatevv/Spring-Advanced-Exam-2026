import CornerFlourish from './CornerFlourish';

export default function ParchmentPanel({children, style, className = ''}) {
    return (
        <div className={`parchment-panel on-parchment ${className}`} style={style}>
            <CornerFlourish corner="tl"/>
            <CornerFlourish corner="tr"/>
            <CornerFlourish corner="bl"/>
            <CornerFlourish corner="br"/>
            {children}
        </div>
    );
}

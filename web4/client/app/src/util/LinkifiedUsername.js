import React from 'react';

const LinkifiedUsername = ({ text }) => {
    if (text === null || text === undefined) {
        return <p></p>;
    } else {
        const paragraphs = text.split('\n');

        const trimmedParagraphs = paragraphs.map(paragraph => paragraph.trim());

        const filteredParagraphs = trimmedParagraphs.filter(paragraph => paragraph !== '');

        const linkifyText = (word) => {
            if (word.startsWith('@')) {
                const username = word.slice(1);
                const profileLink = `/profile/user/${username}`;
                return <a href={profileLink} className='username username-link' style={{ fontWeight: 'bold' }}>{username}</a>;
            }
            return word;
        };

        const linkifiedParagraphs = filteredParagraphs.map((paragraph, index) => (
            <p key={index} style={{ margin: "5px 0" }}>
                {paragraph.split(' ').map((word, wordIndex) => (
                    <React.Fragment key={wordIndex}>
                        {linkifyText(word)}{' '}
                    </React.Fragment>
                ))}
            </p>
        ));

        return (
            <>
                {linkifiedParagraphs}
            </>
        );
    }
};

export default LinkifiedUsername;
export function sortedStringify(obj: unknown): string | undefined {
    const allKeys: string[] = [];

    JSON.stringify(obj, (key, value) => {
        allKeys.push(key);
        return value;
    });

    allKeys.sort();
    return JSON.stringify(obj, allKeys);
}

/*
 * Copyright (c) 2016 Lucko (Luck) <luck@lucko.me>
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

package me.lucko.luckperms.common.calculators;

import me.lucko.luckperms.api.Tristate;
import me.lucko.luckperms.common.constants.Patterns;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

public class RegexProcessor implements PermissionProcessor {
    private Map<String, Boolean> regexPermissions = new ConcurrentHashMap<>();

    @Override
    public Tristate hasPermission(String permission) {
        for (Map.Entry<String, Boolean> e : regexPermissions.entrySet()) {
            Pattern p = Patterns.compile(e.getKey());
            if (p == null) {
                continue;
            }

            if (p.matcher(permission).matches()) {
                return Tristate.fromBoolean(e.getValue());
            }
        }

        return Tristate.UNDEFINED;
    }

    @Override
    public void updateBacking(Map<String, Boolean> map) {
        regexPermissions.clear();
        for (Map.Entry<String, Boolean> e : map.entrySet()) {
            if (!e.getKey().startsWith("r=") && !e.getKey().startsWith("R=")) {
                continue;
            }

            regexPermissions.put(e.getKey().substring(2), e.getValue());
        }
    }
}
